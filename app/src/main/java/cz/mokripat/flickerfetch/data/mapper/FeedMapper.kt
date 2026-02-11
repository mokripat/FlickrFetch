package cz.mokripat.flickerfetch.data.mapper

import cz.mokripat.flickerfetch.data.api.dto.PhotoItemDto
import cz.mokripat.flickerfetch.data.api.dto.PublicFeedDto
import cz.mokripat.flickerfetch.domain.model.PhotoItem
import cz.mokripat.flickerfetch.domain.model.PublicFeed

/**
 * Maps [PublicFeedDto] to domain model [PublicFeed].
 */
internal fun PublicFeedDto.toDomain(): PublicFeed {
    return PublicFeed(
        title = title,
        items = items.map { it.toDomain() }
    )
}

/**
 * Maps [PhotoItemDto] to domain model [PhotoItem].
 * Handles formatting of photo URLs and extracting cleaner author names.
 */
private fun PhotoItemDto.toDomain(): PhotoItem {
    return PhotoItem(
        title = title,
        link = link,
        thumbnailUrl = media.m,
        photoUrl = media.m.replace("_m.", "_b."),
        dateTaken = dateTaken,
        author = author.extractAuthorName(),
        tags = tags.split(" ").filter { it.isNotBlank() }
    )
}

/**
 * Extracts a cleaner author name from the Flickr format.
 * Format usually looks like: nobody@flickr.com ("AuthorName")
 */
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
