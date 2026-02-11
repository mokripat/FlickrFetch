package cz.mokripat.flickerfetch.ui.feed.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import cz.mokripat.flickerfetch.R
import cz.mokripat.flickerfetch.domain.model.PhotoItem

@Composable
internal fun ImageInfoContent(
    photo: PhotoItem
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.image_info_sheet_horizontal_padding))
            .padding(bottom = dimensionResource(R.dimen.image_info_sheet_bottom_padding), top = dimensionResource(R.dimen.image_info_sheet_top_padding))
            .navigationBarsPadding()
    ) {
        val title = photo.title.ifBlank { stringResource(R.string.image_info_sheet_no_title) }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.image_info_sheet_spacer_height_large)))

        InfoRow(label = stringResource(R.string.image_info_sheet_label_author), value = photo.author)
        InfoRow(label = stringResource(R.string.image_info_sheet_label_date_taken), value = photo.dateTaken)

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.image_info_sheet_spacer_height_large)))

        Text(
            text = stringResource(R.string.image_info_sheet_label_tags),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.image_info_sheet_spacer_height_small)))

        if (photo.tags.isEmpty()) {
            Text(
                text = stringResource(R.string.image_info_sheet_no_tags),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Text(
                text = photo.tags.joinToString(", "),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.image_info_sheet_row_vertical_padding))) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
