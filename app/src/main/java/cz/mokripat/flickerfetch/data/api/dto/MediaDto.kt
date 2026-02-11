package cz.mokripat.flickerfetch.data.api.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing media information in the Flickr feed.
 *
 * @property m The URL of the photo (typically small size).
 */
@Serializable
internal data class MediaDto(
    val m: String
)
