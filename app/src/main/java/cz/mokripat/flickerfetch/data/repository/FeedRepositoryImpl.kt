package cz.mokripat.flickerfetch.data.repository

import cz.mokripat.flickerfetch.data.api.PublicFeedApi
import cz.mokripat.flickerfetch.data.mapper.toDomain
import cz.mokripat.flickerfetch.domain.model.PublicFeed
import cz.mokripat.flickerfetch.domain.repository.FeedRepository

class FeedRepositoryImpl(
    private val api: PublicFeedApi
) : FeedRepository {

    override suspend fun getPublicFeed(): Result<PublicFeed> {
        return try {
            val dto = api.getPublicFeed()
            val domain = dto.toDomain()
            Result.success(domain)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
