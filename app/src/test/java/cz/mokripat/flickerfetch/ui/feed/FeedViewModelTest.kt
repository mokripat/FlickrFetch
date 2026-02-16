package cz.mokripat.flickerfetch.ui.feed

import cz.mokripat.flickerfetch.domain.usecase.GetFeedUseCase
import io.mockk.clearAllMocks
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    private lateinit var getFeedUseCase: GetFeedUseCase
    private lateinit var viewModel: FeedViewModel
    private lateinit var testDispatcher: TestDispatcher

    @BeforeEach
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        getFeedUseCase = mockk()

        // Note: FeedViewModel calls loadFeed() in its init block.
        // Since we are using StandardTestDispatcher, the coroutine usually doesn't execute immediately
        // until we call runTest or advanceUntilIdle, giving us time to stub getFeedUseCase if needed,
        // or verify initial state.
        viewModel = FeedViewModel(getFeedUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `addOnTag actually adds tags to the state`() {
        val expectedTags = listOf("tag1", "tag2")

        viewModel.onSearchQueryChange("tag1")
        viewModel.onAddTag()
        viewModel.onSearchQueryChange("tag2")
        viewModel.onAddTag()
        viewModel.onSearchQueryChange("tag2")
        viewModel.onAddTag()
        viewModel.onSearchQueryChange("tag2")
        viewModel.onAddTag()
        viewModel.onSearchQueryChange("tag2")
        viewModel.onAddTag()
        viewModel.onSearchQueryChange("tag2")
        viewModel.onAddTag()

        val tags = viewModel.state.value.tags

        assertEquals(expectedTags, tags)
    }
}
