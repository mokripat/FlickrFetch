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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.mokripat.flickerfetch.domain.model.PhotoItem
import cz.mokripat.flickerfetch.ui.feed.components.FeedSearchBar
import cz.mokripat.flickerfetch.ui.feed.components.PhotoItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreenContent(
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
            isRefreshing = state.isLoading,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                state.isLoading && state.photos.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error: ${state.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                state.photos.isEmpty() -> {
                     Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (state.tags.isEmpty()) {
                                "No photos available \uD83D\uDE31"
                            } else {
                                "No photos matching current tags \uD83D\uDE35"
                            }

                        )
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = contentPaddingValues.calculateBottomPadding(),
                            start = 8.dp,
                            end = 8.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
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