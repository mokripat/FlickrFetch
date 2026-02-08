package cz.mokripat.flickerfetch.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoItemDto(
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
