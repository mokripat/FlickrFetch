package cz.mokripat.flickerfetch.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mokripat.flickerfetch.domain.usecase.GetFeedUseCase
import cz.mokripat.flickerfetch.domain.model.PhotoItem
import cz.mokripat.flickerfetch.domain.model.SortStrategy
import cz.mokripat.flickerfetch.domain.model.TagMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the FeedScreen.
 * Manages state and interacts with [GetFeedUseCase].
 */
internal class FeedViewModel(
    private val getFeedUseCase: GetFeedUseCase,
): ViewModel() {

    private val _strategy = MutableStateFlow(SortStrategy.DEFAULT)

    private val _tagMode = MutableStateFlow(TagMode.ALL)

    private val _state = MutableStateFlow(FeedScreenState())
    private val _effect = Channel<FeedScreenEffect>(Channel.BUFFERED)

    private val _parameterFlow = combine(
        _strategy,
        _tagMode
    ) { strategy, tagMode ->
        Pair(strategy, tagMode)
    }.debounce(300)

    /**
     * StateFlow representing the current state of the feed screen, observed by the UI.
     */
    val state: StateFlow<FeedScreenState> = _state.asStateFlow()

    /**
     * Flow of one-time effects emitted by the ViewModel, such as showing error messages, observed by the UI.
     */
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            _parameterFlow.collect { (strategy, mode) ->
                loadFeed(strategy = strategy, tagMode = mode)
            }
        }

        loadFeed()
    }


    fun onSwitchSortingStrategy() {
        _strategy.value = when(_strategy.value) {
            SortStrategy.DEFAULT -> SortStrategy.BY_DATE
            SortStrategy.BY_DATE -> SortStrategy.DEFAULT
        }
    }

    fun onSwitchTagMode() {
        _tagMode.value = when (_tagMode.value) {
            TagMode.ALL -> TagMode.ANY
            TagMode.ANY -> TagMode.ALL
        }
    }

    /**
     * Loads the photo feed, optionally using the current tags.
     *
     * @param isPullRefresh Whether this load was triggered by a pull-to-refresh action, to show appropriate loading state.
     */
    fun loadFeed(isPullRefresh: Boolean = false, strategy: SortStrategy = _strategy.value, tagMode: TagMode = _tagMode.value) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isPullRefreshing = isPullRefresh,
                )
            }

            // Use current tags from state for search
            val tags = _state.value.tags.ifEmpty { null }
            val result = getFeedUseCase(tags, strategy)

            if (result.isSuccess) {
                val feed = result.getOrThrow()
                _state.update {
                    it.copy(
                        isLoading = false,
                        isPullRefreshing = false,
                        photos = feed.items,
                        error = null,
                        sortStrategy = strategy,
                        tagMode = tagMode,
                    )
                }
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Failed to load feed \uD83D\uDE2D"
                _effect.send(FeedScreenEffect.ShowError(errorMessage))
                _state.update {
                    it.copy(
                        photos = emptyList(),
                        isLoading = false,
                        isPullRefreshing = false,
                        error = errorMessage,
                        sortStrategy = strategy,
                        tagMode = tagMode,
                    )
                }
            }
        }
    }

    /**
     * Selects a photo to show in detail view.
     *
     * @param photo The photo item to select.
     */
    fun selectPhoto(photo: PhotoItem) {
        _state.update { it.copy(selectedPhoto = photo) }
    }

    /**
     * Closes the detail view.
     */
    fun closeDetail() {
        _state.update { it.copy(selectedPhoto = null) }
    }

    /**
     * Updates the search query text.
     *
     * @param query The new search query text.
     */
    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    /**
     * Adds the current search query as a tag and reloads the feed.
     */
    fun onAddTag() {
        val query = _state.value.searchQuery.trim()
        if (query.isNotEmpty() && !_state.value.tags.contains(query)) {
            _state.update {
                it.copy(
                    tags = it.tags + query,
                    searchQuery = ""
                )
            }
            loadFeed()
        } else if (query.isNotEmpty()) {
             // Optional: just clear query if tag exists
             _state.update { it.copy(searchQuery = "") }
        }
    }

    /**
     * Removes a tag and reloads the feed.
     *
     * @param tag The tag to remove.
     */
    fun onRemoveTag(tag: String) {
        _state.update {
            it.copy(tags = it.tags - tag)
        }
        loadFeed()
    }

    /**
     * Toggles the visibility of the search bar.
     * If closing, also clears tags.
     */
    fun onToggleSearch() {
        val isCurrentlySearchVisible = _state.value.isSearchVisible
        if (isCurrentlySearchVisible) {
            onClearTags()
        }
        _state.update { it.copy(isSearchVisible = !isCurrentlySearchVisible) }
    }

    /**
     * Clears all tags and the search query, then reloads the feed.
     *
     * If there are no tags, it does nothing to avoid unwanted reload.
     */
    fun onClearTags() {
        if (_state.value.tags.isEmpty()) {
            return
        }

        _state.update { it.copy(tags = emptyList(), searchQuery = "") }
        loadFeed()
    }
}