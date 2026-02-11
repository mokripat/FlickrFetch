package cz.mokripat.flickerfetch.ui.feed.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil3.compose.SubcomposeAsyncImage
import cz.mokripat.flickerfetch.R

@Composable
internal fun FlickerAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    loadingIndicatorSize: Dp? = null
) {
    val actualLoadingIndicatorSize = loadingIndicatorSize ?: dimensionResource(R.dimen.feed_async_image_loading_indicator_size)
    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(actualLoadingIndicatorSize),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        },
        error = {
            Image(
                painter = painterResource(R.drawable.image_placeholder),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = contentScale
            )
        },
        modifier = modifier,
        contentScale = contentScale
    )
}
