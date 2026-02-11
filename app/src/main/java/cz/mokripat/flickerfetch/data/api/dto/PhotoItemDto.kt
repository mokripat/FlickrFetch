package cz.mokripat.flickerfetch.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing a photo item in the Flickr feed.
 *
 * @property title Title of the photo.
 * @property link Link to the photo page on Flickr.
 * @property media Media information containing image URLs.
 * @property dateTaken Date and time when the photo was taken.
 * @property description HTML description of the photo.
 * @property published Date and time when the photo was published.
 * @property author Author of the photo.
 * @property authorId User ID of the author.
 * @property tags Space-separated list of tags associated with the photo.
 */
@Serializable
internal data class PhotoItemDto(
    val title: String,
    val link: String,
    val media: MediaDto,
    @SerialName("date_taken")
    val dateTaken: String,
    val description: String,
    val published: String,
    val author: String,
    @SerialName("author_id")
    val authorId: String,
    val tags: String
)
