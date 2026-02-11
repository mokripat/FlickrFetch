package cz.mokripat.flickerfetch.ui.feed

import cz.mokripat.flickerfetch.domain.model.PhotoItem

/**
 * State of the FeedScreen.
 *
 * @property isLoading Whether the feed is currently loading.
 * @property photos List of photos to display.
 * @property error Error message if any occurred.
 * @property selectedPhoto The currently selected photo for detail view.
 * @property searchQuery Current text in the search bar.
 * @property tags Current list of active search tags.
 */
internal data class FeedScreenState(
    val isLoading: Boolean = false,
    val photos: List<PhotoItem> = emptyList(),
    val error: String? = null,
    val selectedPhoto: PhotoItem? = null,
    val searchQuery: String = "",
    val tags: List<String> = emptyList()
)
