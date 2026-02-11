package cz.mokripat.flickerfetch.data.api.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class PublicFeedDto(
    val title: String,
    val link: String,
    val description: String,
    val modified: String,
    val generator: String,
    val items: List<PhotoItemDto>
)
