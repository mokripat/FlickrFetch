package cz.mokripat.flickerfetch.domain.usecase

import cz.mokripat.flickerfetch.domain.model.PublicFeed
import cz.mokripat.flickerfetch.domain.model.SortStrategy
import cz.mokripat.flickerfetch.domain.repository.FeedRepository
import java.time.OffsetDateTime

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
    suspend operator fun invoke(tags: List<String>? = null, strategy: SortStrategy = SortStrategy.DEFAULT): Result<PublicFeed>
}

/**
 * Implementation of [GetFeedUseCase].
 */
internal class GetFeedUseCaseImpl(
    private val repository: FeedRepository
) : GetFeedUseCase {
    override suspend fun invoke(tags: List<String>?, strategy: SortStrategy): Result<PublicFeed> {
        val feed = repository.getPublicFeed(tags)

        return if (feed.isSuccess) {
            val data = feed.getOrThrow()
            when (strategy) {
                SortStrategy.BY_DATE -> feed.map { sortByDate(data) }
                SortStrategy.DEFAULT -> feed
            }
        } else {
            feed
        }
    }

    private fun sortByDate(feed: PublicFeed): PublicFeed {
        val sortedItems = feed.items.sortedBy { OffsetDateTime.parse(it.dateString) }
        return feed.copy(items = sortedItems)
    }
}