package cz.mokripat.flickerfetch.domain.usecase

import cz.mokripat.flickerfetch.domain.model.PublicFeed
import cz.mokripat.flickerfetch.domain.repository.FeedRepository

interface GetFeedUseCase {
    suspend operator fun invoke(tags: List<String>? = null): Result<PublicFeed>
}

internal class GetFeedUseCaseImpl(
    private val repository: FeedRepository
) : GetFeedUseCase {
    override suspend fun invoke(tags: List<String>?): Result<PublicFeed> {
        return repository.getPublicFeed(tags)
    }
}