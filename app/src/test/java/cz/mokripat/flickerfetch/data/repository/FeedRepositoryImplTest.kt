package cz.mokripat.flickerfetch.data.repository

import cz.mokripat.flickerfetch.data.api.PublicFeedApi
import cz.mokripat.flickerfetch.data.api.dto.MediaDto
import cz.mokripat.flickerfetch.data.api.dto.PhotoItemDto
import cz.mokripat.flickerfetch.data.api.dto.PublicFeedDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FeedRepositoryImplTest {

    private lateinit var api: PublicFeedApi
    private lateinit var repository: FeedRepositoryImpl

    @BeforeEach
    fun setUp() {
        api = mockk()
        repository = FeedRepositoryImpl(api, StandardTestDispatcher())
    }

    @Test
    fun `getPublicFeed returns success with mapped domain model`() = runTest {
        // Arrange
        val photoItemDto = PhotoItemDto(
            title = "Test Photo",
            link = "https://flickr.com/photo/1",
            media = MediaDto(m = "https://flickr.com/img_m.jpg"),
            dateTaken = "2026-02-11T19:51:00-08:00",
            description = "Description",
            published = "2026-02-11T19:51:00Z",
            author = "nobody@flickr.com (\"Test User\")",
            authorId = "12345",
            tags = "tag1 tag2"
        )
        val feedDto = PublicFeedDto(
            title = "Feed Title",
            link = "https://flickr.com/feed",
            description = "Feed Description",
            modified = "2026-02-11T19:51:00Z",
            generator = "Flickr",
            items = listOf(photoItemDto)
        )

        coEvery { api.getPublicFeed(any(), any(), any()) } returns feedDto

        // Act
        val result = repository.getPublicFeed()

        // Assert
        assertTrue(result.isSuccess)
        val feed = result.getOrThrow()
        assertEquals("Feed Title", feed.title)
        assertEquals(1, feed.items.size)

        val item = feed.items[0]
        assertEquals("Test Photo", item.title)
        assertEquals("https://flickr.com/photo/1", item.link)
        assertEquals("https://flickr.com/img_m.jpg", item.thumbnailUrl)
        assertEquals("https://flickr.com/img_b.jpg", item.photoUrl) // Check substitution
        assertEquals("Test User", item.author) // Check author extraction
        assertEquals(listOf("tag1", "tag2"), item.tags) // Check tag splitting
    }

    @Test
    fun `getPublicFeed passes tags correctly`() = runTest {
        // Arrange
        val tags = listOf("cat", "dog")
        val feedDto = PublicFeedDto(
            title = "Feed",
            link = "",
            description = "",
            modified = "",
            generator = "",
            items = emptyList()
        )
        coEvery { api.getPublicFeed(tags = "cat,dog") } returns feedDto

        // Act
        val result = repository.getPublicFeed(tags)

        // Assert
        assertTrue(result.isSuccess)
        coVerify { api.getPublicFeed(tags = "cat,dog") }
    }

    @Test
    fun `getPublicFeed passes null tags correctly`() = runTest {
        // Arrange
        val feedDto = PublicFeedDto(
            title = "Feed",
            link = "",
            description = "",
            modified = "",
            generator = "",
            items = emptyList()
        )
        coEvery { api.getPublicFeed(tags = null) } returns feedDto

        // Act
        val result = repository.getPublicFeed(null)

        // Assert
        assertTrue(result.isSuccess)
        coVerify { api.getPublicFeed(tags = null) }
    }

    @Test
    fun `getPublicFeed returns failure when api throws exception`() = runTest {
        // Arrange
        val exception = RuntimeException("Network error")
        coEvery { api.getPublicFeed(any(), any(), any()) } throws exception

        // Act
        val result = repository.getPublicFeed()

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
