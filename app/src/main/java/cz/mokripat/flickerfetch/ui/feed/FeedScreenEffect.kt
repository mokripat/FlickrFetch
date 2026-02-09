package cz.mokripat.flickerfetch.ui.feed

sealed class FeedScreenEffect {
    data class ShowError(val message: String) : FeedScreenEffect()
}
