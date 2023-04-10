package com.rskopyl.shake.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import com.rskopyl.shake.ui.theme.ShakeTheme
import com.rskopyl.shake.ui.theme.alpha
import com.rskopyl.shake.ui.theme.dimens

@Composable
fun ShakeFilterChip(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    label: @Composable () -> Unit,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Row(
        horizontalArrangement = Arrangement
            .spacedBy(MaterialTheme.dimens.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .run {
                if (onClick != null) clickable(onClick = onClick)
                else this
            }
            .alpha(if (enabled) 1f else MaterialTheme.alpha.disabledContainer)
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.secondaryContainer
            )
            .padding(MaterialTheme.dimens.small)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides if (selected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSecondaryContainer
            },
            LocalTextStyle provides MaterialTheme.typography.labelLarge
        ) {
            label()
            if (trailingIcon != null) {
                Box(
                    modifier = Modifier
                        .height(MaterialTheme.dimens.large)
                ) {
                    trailingIcon()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShakeFilterChipPreview() {
    ShakeTheme {
        ShakeFilterChip(
            selected = true,
            label = { Text(text = "Ingredient: Tequila") },
            onClick = {},
            trailingIcon = {
                Icon(Icons.Default.Close, contentDescription = null)
            }
        )
    }
}