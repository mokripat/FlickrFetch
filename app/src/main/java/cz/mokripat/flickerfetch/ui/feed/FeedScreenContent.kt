package cz.mokripat.flickerfetch.ui.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import cz.mokripat.flickerfetch.R
import cz.mokripat.flickerfetch.domain.model.PhotoItem
import cz.mokripat.flickerfetch.ui.feed.components.FeedSearchBar
import cz.mokripat.flickerfetch.ui.feed.components.PhotoItemCard

/**
 * Content of the FeedScreen, displaying the search bar and the list of photos.
 *
 * @param state Current state of the screen.
 * @param contentPaddingValues Padding values from the scaffold.
 * @param isSearchVisible Whether the search bar is visible.
 * @param onRefresh Callback to refresh the feed.
 * @param onPhotoClick Callback when a photo is clicked.
 * @param onSearchQueryChange Callback when search query changes.
 * @param onAddTag Callback to add a tag.
 * @param onRemoveTag Callback to remove a tag.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FeedScreenContent(
    state: FeedScreenState,
    contentPaddingValues: PaddingValues,
    isSearchVisible: Boolean,
    onRefresh: () -> Unit,
    onPhotoClick: (PhotoItem) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = contentPaddingValues.calculateTopPadding())
    ) {
        AnimatedVisibility(
            visible = isSearchVisible,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            FeedSearchBar(
                query = state.searchQuery,
                tags = state.tags,
                onQueryChange = onSearchQueryChange,
                onAddTag = onAddTag,
                onRemoveTag = onRemoveTag
            )
        }

        PullToRefreshBox(
            isRefreshing = state.isPullRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                state.isLoading && state.photos.isEmpty() -> {
                    FullscreenLoading()
                }
                state.error != null -> {
                    ErrorMessage(error = state.error)
                }
                state.photos.isEmpty() -> {
                    EmptyStateMessage(hasTags = state.tags.isNotEmpty())
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = contentPaddingValues.calculateBottomPadding(),
                            start = dimensionResource(R.dimen.feed_screen_content_grid_spacing),
                            end = dimensionResource(R.dimen.feed_screen_content_grid_spacing)
                        ),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.feed_screen_content_grid_spacing)),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.feed_screen_content_grid_spacing))
                    ) {
                        items(state.photos) { photo ->
                            PhotoItemCard(
                                photo = photo,
                                onClick = { onPhotoClick(photo) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FullscreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorMessage(error: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = dimensionResource(R.dimen.feed_screen_error_horizontal_spacing)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.feed_screen_content_error_prefix, error),
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun EmptyStateMessage(hasTags: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (!hasTags) {
                stringResource(R.string.feed_screen_content_no_photos)
            } else {
                stringResource(R.string.feed_screen_content_no_photos_matching_tags)
            }

        )
    }
}
