package com.rskopyl.shake.ui.custom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.rskopyl.shake.ui.theme.ShakeTheme
import com.rskopyl.shake.ui.theme.dimens
import com.rskopyl.shake.util.ProvideContentColor

@Composable
fun ShakeAlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(),
    content: @Composable (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            ProvideContentColor(
                MaterialTheme.colorScheme.onBackground,
                content = confirmButton
            )
        },
        modifier = modifier,
        dismissButton = dismissButton?.let {
            {
                ProvideContentColor(
                    MaterialTheme.colorScheme.onBackground,
                    content = dismissButton
                )
            }
        },
        icon = icon,
        title = title?.let {
            {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProvideTextStyle(
                        MaterialTheme.typography.titleLarge,
                        content = title
                    )
                }
            }
        },
        text = content?.let {
            {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.dimens.large)
                ) {
                    ProvideTextStyle(
                        MaterialTheme.typography.bodyMedium,
                        content = content
                    )
                }
            }
        },
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.background,
        iconContentColor = MaterialTheme.colorScheme.onBackground,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
        textContentColor = MaterialTheme.colorScheme.onBackground,
        properties = properties
    )
}

@Preview
@Composable
private fun ShakeAlertDialogPreview() {
    ShakeTheme {
        ShakeAlertDialog(
            onDismissRequest = {},
            confirmButton = {
                ShakeTextButton(onClick = {}) {
                    Text("Select")
                }
            },
            title = { Text("Category") },
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ordinary Drinks")
                }
            }
        )
    }
}