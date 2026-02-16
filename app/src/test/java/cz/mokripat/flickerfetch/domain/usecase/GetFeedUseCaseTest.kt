package cz.mokripat.flickerfetch.domain.usecase

import cz.mokripat.flickerfetch.domain.model.PhotoItem
import cz.mokripat.flickerfetch.domain.model.PublicFeed
import cz.mokripat.flickerfetch.domain.model.SortStrategy
import cz.mokripat.flickerfetch.domain.repository.FeedRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetFeedUseCaseTest {

    private lateinit var feedRepository: FeedRepository
    private lateinit var getFeedUseCase: GetFeedUseCase

    @BeforeEach
    fun setUp() {
        feedRepository = mockk()
        getFeedUseCase = GetFeedUseCaseImpl(feedRepository)
    }

    @Test
    fun `invoke with SortStrategy_BY_DATE returns items sorted by date ascending`() = runTest {
        // Arrange
        val itemMiddle = createPhotoItem("2026-02-08T10:00:00-08:00")
        val itemLatest = createPhotoItem("2026-02-09T10:00:00-08:00")
        val itemEarliest = createPhotoItem("2026-02-07T10:00:00-08:00")

        val unsortedItems = listOf(itemMiddle, itemLatest, itemEarliest)
        val feed = PublicFeed("Test Feed", unsortedItems)

        coEvery { feedRepository.getPublicFeed(any()) } returns Result.success(feed)

        // Act
        val result = getFeedUseCase(strategy = SortStrategy.BY_DATE)

        // Assert
        assert(result.isSuccess)
        val sortedItems = result.getOrThrow().items
        assertEquals(3, sortedItems.size)
        assertEquals(itemEarliest, sortedItems[0]) // Earliest first (ascending)
        assertEquals(itemMiddle, sortedItems[1])
        assertEquals(itemLatest, sortedItems[2])
    }

    @Test
    fun `invoke with SortStrategy_DEFAULT returns items in original order`() = runTest {
        // Arrange
        val item1 = createPhotoItem("2026-02-08T10:00:00-08:00")
        val item2 = createPhotoItem("2026-02-09T10:00:00-08:00")
        val item3 = createPhotoItem("2026-02-07T10:00:00-08:00")

        val unsortedItems = listOf(item1, item2, item3)
        val feed = PublicFeed("Test Feed", unsortedItems)

        coEvery { feedRepository.getPublicFeed(any()) } returns Result.success(feed)

        // Act
        val result = getFeedUseCase(strategy = SortStrategy.DEFAULT)

        // Assert
        assert(result.isSuccess)
        val items = result.getOrThrow().items
        assertEquals(3, items.size)
        assertEquals(item1, items[0])
        assertEquals(item2, items[1])
        assertEquals(item3, items[2])
    }

    @Test
    fun `if fetch error happens, we will get error result`() = runTest {
        // Arrange
        coEvery { feedRepository.getPublicFeed(any()) } returns Result.failure(Exception("Network error"))

        // Act
        val result = getFeedUseCase(strategy = SortStrategy.DEFAULT)

        // Assert
        assert(result.isFailure)
    }

    // Helper
    private fun createPhotoItem(dateTaken: String): PhotoItem {
        return PhotoItem(
            title = "Test",
            link = "http://link.com",
            thumbnailUrl = "http://thumb.com",
            photoUrl = "http://photo.com",
            dateTaken = dateTaken,
            dateString = "dateString",
            author = "author",
            tags = emptyList()
        )
    }
}
