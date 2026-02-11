package cz.mokripat.flickerfetch.data.repository

import cz.mokripat.flickerfetch.data.api.PublicFeedApi
import cz.mokripat.flickerfetch.data.mapper.toDomain
import cz.mokripat.flickerfetch.domain.model.PublicFeed
import cz.mokripat.flickerfetch.domain.repository.FeedRepository

/**
 * Implementation of [FeedRepository].
 * Uses [PublicFeedApi] to fetch data and maps it to domain models.
 */
internal class FeedRepositoryImpl(
    private val api: PublicFeedApi
) : FeedRepository {

    /**
     * Fetches the public feed, optionally filtered by tags.
     *
     * @param tags List of tags to filter by.
     * @return [Result] containing [PublicFeed] on success or error.
     */
    override suspend fun getPublicFeed(tags: List<String>?): Result<PublicFeed> {
        return try {
            val tagsString = tags?.joinToString(separator = ",")
            val dto = api.getPublicFeed(tags = tagsString)
            val domain = dto.toDomain()
            Result.success(domain)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
