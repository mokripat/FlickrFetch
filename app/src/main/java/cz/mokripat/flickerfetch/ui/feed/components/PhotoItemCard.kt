package cz.mokripat.flickerfetch.ui.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import cz.mokripat.flickerfetch.R
import cz.mokripat.flickerfetch.domain.model.PhotoItem

/**
 * Card component for displaying a single photo item in the grid.
 *
 * @param photo The photo item data.
 * @param onClick Callback when the card is clicked.
 */
@Composable
internal fun PhotoItemCard(
    photo: PhotoItem,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            FlickerAsyncImage(
                model = photo.thumbnailUrl,
                contentDescription = photo.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loadingIndicatorSize = dimensionResource(R.dimen.photo_item_card_loading_indicator_size)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
                    .padding(dimensionResource(R.dimen.photo_item_card_text_padding))
            ) {
                Text(
                    text = photo.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoItemCardPreview() {
    MaterialTheme {
        PhotoItemCard(
            photo = PhotoItem(
                title = "Beautiful Sunset Over Mountains",
                link = "https://example.com",
                thumbnailUrl = "https://picsum.photos/400/400",
                photoUrl = "https://picsum.photos/800/800",
                dateTaken = "2026-02-10T15:30:00Z",
                dateString = "2026-02-10T15:30:00Z",
                author = "John Doe",
                tags = listOf("sunset", "mountains", "nature")
            ),
            onClick = {}
        )
    }
}
