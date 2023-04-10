package com.rskopyl.shake.ui.custom

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rskopyl.shake.ui.theme.ShakeTheme
import com.rskopyl.shake.ui.theme.alpha

@Composable
fun ShakeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember {
        MutableInteractionSource()
    },
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.primaryContainer
                .copy(alpha = MaterialTheme.alpha.disabledContent),
            disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
                .copy(alpha = MaterialTheme.alpha.disabledContainer)
        ),
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = {
            ProvideTextStyle(
                MaterialTheme.typography.titleSmall,
                content = { content() }
            )
        }
    )
}

@Preview
@Composable
private fun ShakeButtonPreview() {
    ShakeTheme {
        ShakeButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save")
        }
    }
}