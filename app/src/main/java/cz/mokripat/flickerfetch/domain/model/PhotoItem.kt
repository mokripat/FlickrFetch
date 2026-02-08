package cz.mokripat.flickerfetch.domain.model

data class PhotoItem(
    val title: String,
    val link: String,
    val imageUrl: String,
    val dateTaken: String,
    val author: String,
    val tags: List<String>
)
