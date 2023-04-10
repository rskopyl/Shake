package com.rskopyl.shake.ui.screen.shaker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.rskopyl.shake.R
import com.rskopyl.shake.ui.custom.ShakeAlertDialog
import com.rskopyl.shake.ui.custom.ShakeTextButton
import com.rskopyl.shake.ui.custom.ShakeTextField
import com.rskopyl.shake.ui.theme.ShakeTheme
import com.rskopyl.shake.ui.theme.dimens

@Composable
fun ShakerTagDialog(
    onNewTagConfirm: (String) -> Unit,
    onNewTagDismiss: () -> Unit
) {
    var tag by rememberSaveable { mutableStateOf("") }

    ShakerTagDialog(
        tag = tag,
        onTagValueChange = { value -> tag = value },
        onNewTagConfirm = { onNewTagConfirm(tag) },
        onNewTagDismiss = onNewTagDismiss
    )
}

@Composable
private fun ShakerTagDialog(
    tag: String,
    onTagValueChange: (String) -> Unit,
    onNewTagConfirm: () -> Unit,
    onNewTagDismiss: () -> Unit
) {
    ShakeAlertDialog(
        title = {
            Text(stringResource(R.string.shaker_new_tag))
        },
        confirmButton = {
            ShakeTextButton(onClick = onNewTagConfirm) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            ShakeTextButton(onClick = onNewTagDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        onDismissRequest = onNewTagDismiss
    ) {
        ShakeTextField(
            value = tag,
            onValueChange = onTagValueChange,
            placeholder = {
                Text(
                    stringResource(R.string.shaker_tag_placeholder),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge
                .copy(textAlign = TextAlign.Center),
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.extraLarge)
        )
    }
}

@Preview
@Composable
private fun ShakerTagDialogPreview() {
    ShakeTheme {
        ShakerTagDialog(
            tag = "New Era Drink",
            onTagValueChange = {},
            onNewTagConfirm = {},
            onNewTagDismiss = {}
        )
    }
}