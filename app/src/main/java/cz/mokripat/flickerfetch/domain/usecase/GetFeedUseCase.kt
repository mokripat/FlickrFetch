package cz.mokripat.flickerfetch.domain.usecase

import cz.mokripat.flickerfetch.domain.model.PublicFeed
import cz.mokripat.flickerfetch.domain.repository.FeedRepository

/**
 * Use case for retrieving the public photo feed.
 */
interface GetFeedUseCase {
    /**
     * Executes the use case.
     *
     * @param tags Optional list of tags to filter the feed.
     * @return [Result] containing the [PublicFeed].
     */
    suspend operator fun invoke(tags: List<String>? = null): Result<PublicFeed>
}

/**
 * Implementation of [GetFeedUseCase].
 */
internal class GetFeedUseCaseImpl(
    private val repository: FeedRepository
) : GetFeedUseCase {
    override suspend fun invoke(tags: List<String>?): Result<PublicFeed> {
        return repository.getPublicFeed(tags)
    }
}