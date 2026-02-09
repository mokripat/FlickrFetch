package cz.mokripat.flickerfetch.ui.feed

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun FeedScreen(
    viewModel: FeedViewModel
) {
    val data by viewModel.state.collectAsState()

    Text(
        text = data.toString()
    )
}