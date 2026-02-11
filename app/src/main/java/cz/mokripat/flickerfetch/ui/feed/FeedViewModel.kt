package cz.mokripat.flickerfetch.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mokripat.flickerfetch.domain.usecase.GetFeedUseCase
import cz.mokripat.flickerfetch.domain.model.PhotoItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _state = MutableStateFlow(FeedScreenState())
    val state: StateFlow<FeedScreenState> = _state.asStateFlow()

    private val _effect = Channel<FeedScreenEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadFeed()
    }

    /**
     * Loads the photo feed, optionally using the current tags.
     */
    fun loadFeed() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // Use current tags from state for search
            val tags = _state.value.tags.ifEmpty { null }
            val result = getFeedUseCase(tags)

            _state.update {
                if (result.isSuccess) {
                    val feed = result.getOrNull()!!
                    it.copy(
                        isLoading = false,
                        photos = feed.items,
                        error = null
                    )
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                    it.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            }
        }
    }

    /**
     * Selects a photo to show in detail view.
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
     */
    fun onRemoveTag(tag: String) {
        _state.update {
            it.copy(tags = it.tags - tag)
        }
        loadFeed()
    }

    /**
     * Clears all tags and the search query, then reloads the feed.
     */
    fun onClearTags() {
        _state.update { it.copy(tags = emptyList(), searchQuery = "") }
        loadFeed()
    }
}