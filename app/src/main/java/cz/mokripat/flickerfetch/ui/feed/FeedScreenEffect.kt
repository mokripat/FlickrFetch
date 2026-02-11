package cz.mokripat.flickerfetch.ui.feed

/**
 * Side effects invoked by the ViewModel.
 */
internal sealed class FeedScreenEffect {
    /**
     * Show an error message.
     */
    data class ShowError(val message: String) : FeedScreenEffect()
}
