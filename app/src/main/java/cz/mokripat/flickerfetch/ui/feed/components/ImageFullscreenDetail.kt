package cz.mokripat.flickerfetch.ui.feed.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import cz.mokripat.flickerfetch.domain.model.PhotoItem
import cz.mokripat.flickerfetch.ui.feed.sheet.ImageInfoSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageFullscreenDetail(
    photo: PhotoItem,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)

    var showInfo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showInfo = true }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AsyncImage(
                model = photo.imageUrl,
                contentDescription = photo.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }

    if (showInfo) {
        ImageInfoSheet(
            photo = photo,
            onDismissRequest = { showInfo = false }
        )
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
                imageUrl = "https://picsum.photos/400/400",
                dateTaken = "2026-02-10T15:30:00Z",
                author = "John Doe",
                tags = listOf("sunset", "mountains", "nature")
            ),
            onBack = {}
        )
    }
}
