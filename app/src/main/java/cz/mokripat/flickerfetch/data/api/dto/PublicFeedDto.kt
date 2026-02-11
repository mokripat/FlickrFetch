package cz.mokripat.flickerfetch.data.api.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing the root response of the Flickr Public Feed.
 *
 * @property title Title of the feed.
 * @property link Link to the feed on Flickr.
 * @property description Description of the feed.
 * @property modified Date and time when the feed was last modified.
 * @property generator Generator of the feed.
 * @property items List of photo items in the feed.
 */
@Serializable
internal data class PublicFeedDto(
    val title: String,
    val link: String,
    val description: String,
    val modified: String,
    val generator: String,
    val items: List<PhotoItemDto>
)
