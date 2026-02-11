package cz.mokripat.flickerfetch.ui.feed.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import cz.mokripat.flickerfetch.R

/**
 * Search bar component with support for tags.
 *
 * @param query Current search query text.
 * @param tags List of active tags.
 * @param onQueryChange Callback when query changes.
 * @param onAddTag Callback to add the current query as a tag.
 * @param onRemoveTag Callback to remove a specific tag.
 * @param modifier Component modifier.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun FeedSearchBar(
    query: String,
    tags: List<String>,
    onQueryChange: (String) -> Unit,
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.feed_search_bar_horizontal_padding))
            .padding(bottom = dimensionResource(R.dimen.feed_search_bar_bottom_padding))
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.feed_search_bar_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = CircleShape,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { onAddTag() }
            )
        )

        AnimatedVisibility(visible = tags.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.feed_search_bar_tags_top_padding)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.feed_search_bar_tags_spacing)),
            ) {
                tags.forEach { tag ->
                    InputChip(
                        selected = false,
                        onClick = { },
                        label = { Text(tag, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        shape = CircleShape,
                        trailingIcon = {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(R.string.feed_search_bar_remove_tag_content_description),
                                modifier = Modifier
                                    .padding(start = dimensionResource(R.dimen.feed_search_bar_remove_tag_icon_padding))
                                    .clickable { onRemoveTag(tag) }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedSearchBarPreview() {
    MaterialTheme {
        FeedSearchBar(
            query = "Nature",
            tags = listOf("sunset", "mountains", "river"),
            onQueryChange = {},
            onAddTag = {},
            onRemoveTag = {}
        )
    }
}
