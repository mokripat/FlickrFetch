package cz.mokripat.flickerfetch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cz.mokripat.flickerfetch.ui.feed.FeedScreen
import cz.mokripat.flickerfetch.ui.theme.FlickerFetchTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * Main activity of FlickerFetch app.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlickerFetchTheme {
                FeedScreen(
                    viewModel = getViewModel(),
                )
            }
        }
    }
}