package cz.mokripat.flickerfetch.ui.feed

sealed class FeedEffect {
    data class ShowError(val message: String) : FeedEffect()
}
