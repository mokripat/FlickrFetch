package cz.mokripat.flickerfetch.domain.repository

import cz.mokripat.flickerfetch.domain.model.PublicFeed

/**
 * Repository interface for accessing feed data.
 */
interface FeedRepository {

    /**
     * Fetches the public feed from the data source, optionally filtered by tags.
     *
     * @param tags Optional list of tags to filter the feed.
     * @return [Result] containing [PublicFeed] if successful, or an exception if failed.
     */
    suspend fun getPublicFeed(tags: List<String>? = null): Result<PublicFeed>
}
