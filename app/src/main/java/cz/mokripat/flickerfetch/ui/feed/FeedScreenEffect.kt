package cz.mokripat.flickerfetch.ui.feed

internal sealed class FeedScreenEffect {
    data class ShowError(val message: String) : FeedScreenEffect()
}
