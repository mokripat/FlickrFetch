package cz.mokripat.flickerfetch.domain.model

/**
 * Domain model representing a photo item.
 *
 * @property title Title of the photo.
 * @property link Link to the photo on Flickr.
 * @property thumbnailUrl URL of the thumbnail image (Small).
 * @property photoUrl URL of the full-size image (Large).
 * @property dateTaken Date and time when the photo was taken.
 * @property author Author of the photo.
 * @property tags List of tags associated with the photo.
 */
data class PhotoItem(
    val title: String,
    val link: String,
    val thumbnailUrl: String,
    val photoUrl: String,
    val dateTaken: String,
    val author: String,
    val tags: List<String>
)
