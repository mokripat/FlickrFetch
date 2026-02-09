package cz.mokripat.flickerfetch.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mokripat.flickerfetch.domain.model.PublicFeed
import cz.mokripat.flickerfetch.domain.usecase.GetFeedUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class FeedViewModel(
    getFeedUseCase: GetFeedUseCase,
): ViewModel() {
    val state : StateFlow<PublicFeed> = flow {
        val result = getFeedUseCase()
        if (result.isSuccess) {
            emit(result.getOrNull()!!)
        } else {
            // Handle error case, e.g. emit an empty feed or a default value
            emit(PublicFeed(title = "Error", items = emptyList()))
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, PublicFeed(title = "Loading...", items = emptyList()))
}