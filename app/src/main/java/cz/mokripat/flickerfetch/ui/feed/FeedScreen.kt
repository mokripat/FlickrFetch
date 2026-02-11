package cz.mokripat.flickerfetch.ui.feed

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mokripat.flickerfetch.R
import cz.mokripat.flickerfetch.ui.feed.components.ImageFullscreenDetail

/**
 * Screen to display the feed of photos with search functionality and photo details.
 *
 * @param viewModel The ViewModel that holds the state and logic for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FeedScreen(
    viewModel: FeedViewModel
) {
    val state by viewModel.state.collectAsState()
    var isSearchVisible by rememberSaveable { mutableStateOf(false) }

    ProcessEffects(viewModel)

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                FeedTopAppBar(
                    isSearchVisible = isSearchVisible,
                    onSearchToggle = {
                        if (isSearchVisible) {
                            viewModel.onClearTags()
                        }
                        isSearchVisible = !isSearchVisible
                    }
                )
            }
        ) { innerPadding ->
            FeedScreenContent(
                state = state,
                contentPaddingValues = innerPadding,
                isSearchVisible = isSearchVisible,
                onRefresh = { viewModel.loadFeed(isPullRefresh = true) },
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

@Composable
private fun ProcessEffects(
    viewModel: FeedViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FeedScreenEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeedTopAppBar(
    isSearchVisible: Boolean,
    onSearchToggle: () -> Unit
) {
    TopAppBar(
        title = {
            Icon(
                painterResource(
                    if (isSearchVisible) R.drawable.ic_search_title else R.drawable.ic_feed_title
                ),
                tint = Color.Unspecified,
                modifier = Modifier.height(24.dp),
                contentDescription = null
            )
        },
        actions = {
            IconButton(onClick = onSearchToggle) {
                if (isSearchVisible) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(R.string.feed_screen_content_description_close_search))
                } else {
                    Icon(Icons.Default.Search, contentDescription = stringResource(R.string.feed_screen_content_description_search))
                }
            }
        }
    )
}
