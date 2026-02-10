package cz.mokripat.flickerfetch.ui.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cz.mokripat.flickerfetch.ui.feed.components.ImageFullscreenDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel
) {
    val state by viewModel.state.collectAsState()
    var isSearchVisible by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(if (isSearchVisible) "Search" else "Feed") },
                    actions = {
                        IconButton(onClick = {
                            if (isSearchVisible) {
                                viewModel.onClearTags()
                            }
                            isSearchVisible = !isSearchVisible
                        }) {
                            if (isSearchVisible) {
                                Icon(Icons.Default.Close, contentDescription = "Close Search")
                            } else {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            FeedScreenContent(
                state = state,
                contentPaddingValues = innerPadding,
                isSearchVisible = isSearchVisible,
                onRefresh = { viewModel.loadFeed() },
                onPhotoClick = { viewModel.selectPhoto(it) },
                onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                onAddTag = { viewModel.onAddTag() },
                onRemoveTag = { viewModel.onRemoveTag(it) }
            )
        }

        val selectedPhoto = state.selectedPhoto
        if (selectedPhoto != null) {
            ImageFullscreenDetail(
                photo = selectedPhoto,
                onBack = { viewModel.closeDetail() }
            )
        }
    }
}
