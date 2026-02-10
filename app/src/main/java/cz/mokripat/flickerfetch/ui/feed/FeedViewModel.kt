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

class FeedViewModel(
    private val getFeedUseCase: GetFeedUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(FeedScreenState())
    val state: StateFlow<FeedScreenState> = _state.asStateFlow()

    private val _effect = Channel<FeedScreenEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadFeed()
    }

    fun loadFeed() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = getFeedUseCase()

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

    fun selectPhoto(photo: PhotoItem) {
        _state.update { it.copy(selectedPhoto = photo) }
    }

    fun closeDetail() {
        _state.update { it.copy(selectedPhoto = null) }
    }
}