package cz.mokripat.flickerfetch.data.repository

import cz.mokripat.flickerfetch.data.api.PublicFeedApi
import cz.mokripat.flickerfetch.data.mapper.toDomain
import cz.mokripat.flickerfetch.domain.model.PublicFeed
import cz.mokripat.flickerfetch.domain.repository.FeedRepository

class FeedRepositoryImpl(
    private val api: PublicFeedApi
) : FeedRepository {

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
