package com.rskopyl.shake.ui.custom

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rskopyl.shake.ui.theme.ShakeTheme

@Composable
fun ShakeNavigationBar(
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = modifier,
        content = content
    )
}

@Composable
fun RowScope.ShakeNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    interactionSource: MutableInteractionSource = remember {
        MutableInteractionSource()
    }
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = icon,
        modifier = modifier,
        enabled = enabled,
        label = label?.let {
            {
                ProvideTextStyle(
                    MaterialTheme.typography.labelMedium,
                    content = label
                )
            }
        },
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        interactionSource = interactionSource
    )
}

@Preview
@Composable
private fun ShakeNavigationBarPreview() {
    ShakeTheme {
        ShakeNavigationBar(modifier = Modifier.fillMaxWidth()) {
            ShakeNavigationBarItem(
                selected = true,
                icon = {
                    Icon(Icons.Outlined.Home, contentDescription = null)
                },
                label = { Text("Home") },
                onClick = {}
            )
            ShakeNavigationBarItem(
                selected = false,
                icon = {
                    Icon(Icons.Outlined.Search, contentDescription = null)
                },
                label = { Text("Search") },
                onClick = {}
            )
        }
    }
}