package cz.mokripat.flickerfetch.data.repository

import cz.mokripat.flickerfetch.data.api.PublicFeedApi
import cz.mokripat.flickerfetch.data.mapper.toDomain
import cz.mokripat.flickerfetch.domain.model.PublicFeed
import cz.mokripat.flickerfetch.domain.repository.FeedRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * Implementation of [FeedRepository].
 * Uses [PublicFeedApi] to fetch data and maps it to domain models.
 */
internal class FeedRepositoryImpl(
    private val api: PublicFeedApi,
    private val dispatcher: CoroutineDispatcher,
) : FeedRepository {

    /**
     * Fetches the public feed, optionally filtered by tags.
     *
     * @param tags List of tags to filter by.
     * @return [Result] containing [PublicFeed] on success or error.
     */
    override suspend fun getPublicFeed(tags: List<String>?): Result<PublicFeed> = withContext(dispatcher) {
        try {
            val tagsString = tags?.joinToString(separator = ",")
            val dto = api.getPublicFeed(tags = tagsString)
            val domain = dto.toDomain()
            Result.success(domain)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
