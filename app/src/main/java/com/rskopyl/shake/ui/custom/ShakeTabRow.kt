package com.rskopyl.shake.ui.custom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.rskopyl.shake.ui.theme.ShakeTheme
import com.rskopyl.shake.ui.theme.dimens

@Composable
fun ShakeTabRow(
    modifier: Modifier = Modifier,
    tabs: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement
            .spacedBy(MaterialTheme.dimens.extraLarge),
        modifier = modifier.fillMaxWidth(),
        content = tabs
    )
}

@Composable
fun RowScope.ShakeTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement
            .spacedBy(MaterialTheme.dimens.small),
        modifier = modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(top = MaterialTheme.dimens.small)
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.titleSmall,
            LocalContentColor provides if (selected) {
                MaterialTheme.colorScheme.onBackground
            } else {
                MaterialTheme.colorScheme.outline
            }
        ) {
            content()
            TabRowDefaults.Indicator(
                height = MaterialTheme.dimens.stroke,
                color = if (selected) LocalContentColor.current
                else Color.Transparent
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShakeTabRowPreview() {
    ShakeTheme {
        ShakeTabRow {
            ShakeTab(selected = true, onClick = {}) {
                Text("Saved")
            }
            ShakeTab(selected = false, onClick = {}) {
                Text("History")
            }
            ShakeTab(selected = false, onClick = {}) {
                Text("Custom")
            }
        }
    }
}