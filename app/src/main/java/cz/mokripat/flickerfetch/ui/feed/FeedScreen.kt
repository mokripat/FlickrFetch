package cz.mokripat.flickerfetch.ui.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FeedScreen(
    viewModel: FeedViewModel
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoading && state.photos.isEmpty() -> {
                        CircularProgressIndicator()
                    }

                    state.error != null -> {
                        Text(
                            text = "Error: ${state.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    state.photos.isEmpty() -> {
                        Text(text = "No photos available")
                    }

                    else -> {
                        FeedScreenContent(
                            state = state,
                            contentPaddingValues = PaddingValues(
                                top = innerPadding.calculateTopPadding(),
                                bottom = innerPadding.calculateBottomPadding(),
                                start = 16.dp,
                                end = 16.dp,
                            ),
                            onRefresh = { viewModel.loadFeed() },
                            onPhotoClick = { viewModel.selectPhoto(it) }
                        )
                    }
                }
            }
        }

        if (state.selectedPhoto != null) {
            ImageFullscreenDetail(
                photo = state.selectedPhoto!!,
                onBack = { viewModel.closeDetail() }
            )
        }
    }
}
