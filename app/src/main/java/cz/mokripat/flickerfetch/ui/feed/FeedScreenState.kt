package cz.mokripat.flickerfetch.ui.feed

import cz.mokripat.flickerfetch.domain.model.PhotoItem

data class FeedScreenState(
    val isLoading: Boolean = false,
    val photos: List<PhotoItem> = emptyList(),
    val error: String? = null,
    val selectedPhoto: PhotoItem? = null,
    val searchQuery: String = "",
    val tags: List<String> = emptyList()
)
