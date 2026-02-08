package cz.mokripat.flickerfetch.data.mapper

import cz.mokripat.flickerfetch.data.api.dto.PhotoItemDto
import cz.mokripat.flickerfetch.data.api.dto.PublicFeedDto
import cz.mokripat.flickerfetch.domain.model.PhotoItem
import cz.mokripat.flickerfetch.domain.model.PublicFeed

fun PublicFeedDto.toDomain(): PublicFeed {
    return PublicFeed(
        title = title,
        items = items.map { it.toDomain() }
    )
}

private fun PhotoItemDto.toDomain(): PhotoItem {
    return PhotoItem(
        title = title,
        link = link,
        imageUrl = media.m,
        dateTaken = dateTaken,
        author = author.extractAuthorName(),
        tags = tags.split(" ").filter { it.isNotBlank() }
    )
}

private fun String.extractAuthorName(): String {
    // Extract name from format: nobody@flickr.com ("AuthorName")
    val startIndex = indexOf("(\"")
    val endIndex = indexOf("\")")
    return if (startIndex != -1 && endIndex != -1) {
        substring(startIndex + 2, endIndex)
    } else {
        this
    }
}
