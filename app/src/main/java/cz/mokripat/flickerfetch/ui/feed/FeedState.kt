package cz.mokripat.flickerfetch.ui.feed

import cz.mokripat.flickerfetch.domain.model.PhotoItem

data class FeedState(
    val isLoading: Boolean = false,
    val photos: List<PhotoItem> = emptyList(),
    val error: String? = null,
)
