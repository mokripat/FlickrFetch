package cz.mokripat.flickerfetch.domain.model

/**
 * Domain model representing the public feed.
 *
 * @property title Title of the feed.
 * @property items List of photo items contained in the feed.
 */
data class PublicFeed(
    val title: String,
    val items: List<PhotoItem>
)
