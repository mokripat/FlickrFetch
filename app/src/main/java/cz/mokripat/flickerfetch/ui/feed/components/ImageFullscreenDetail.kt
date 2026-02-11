package cz.mokripat.flickerfetch.ui.feed.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cz.mokripat.flickerfetch.R
import cz.mokripat.flickerfetch.domain.model.PhotoItem
import cz.mokripat.flickerfetch.ui.feed.sheet.ImageInfoContent
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.CircleShape

/**
 * Fullscreen detail view for a photo.
 * Features a bottom sheet for extra information.
 *
 * @param photo The photo item to display.
 * @param onBack Callback when back/close is requested.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ImageFullscreenDetail(
    photo: PhotoItem,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            ImageInfoContent(
                photo = photo
            )
        },
        sheetPeekHeight = dimensionResource(R.dimen.image_fullscreen_detail_sheet_peek_height),
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        containerColor = Color.Transparent,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        scope.launch {
                            if (scaffoldState.bottomSheetState.isVisible) {
                                scaffoldState.bottomSheetState.hide()
                            }
                        }
                    }
                )
                .padding(paddingValues)
        ) {
            FlickerAsyncImage(
                model = photo.photoUrl,
                contentDescription = photo.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
                loadingIndicatorSize = dimensionResource(R.dimen.image_fullscreen_detail_loading_indicator_size)
            )

            // Close Button
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(dimensionResource(R.dimen.image_fullscreen_detail_close_button_padding))
                    .background(
                        color = Color.Black.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.image_fullscreen_detail_close_button_content_description),
                    tint = Color.White
                )
            }

            // Info Button
            Button(
                onClick = {
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(dimensionResource(R.dimen.image_fullscreen_detail_info_button_padding))
                    .navigationBarsPadding(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.image_fullscreen_detail_info_icon_padding))
                )
                Text(stringResource(R.string.image_fullscreen_detail_info_button_text))
            }
        }
    }
}

@Preview
@Composable
private fun ImageDetailScreenPreview() {
    MaterialTheme {
        ImageFullscreenDetail(
            photo = PhotoItem(
                title = "Beautiful Sunset Over Mountains",
                link = "https://example.com",
                thumbnailUrl = "https://picsum.photos/400/400",
                photoUrl = "https://picsum.photos/800/800",
                dateTaken = "2026-02-10T15:30:00Z",
                author = "John Doe",
                tags = listOf("sunset", "mountains", "nature")
            ),
            onBack = {}
        )
    }
}
