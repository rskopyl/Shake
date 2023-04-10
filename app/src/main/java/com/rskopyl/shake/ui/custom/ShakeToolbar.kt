package com.rskopyl.shake.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rskopyl.shake.R
import com.rskopyl.shake.ui.theme.ShakeTheme
import com.rskopyl.shake.ui.theme.dimens

@Composable
fun ShakeToolbar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = @Composable {
        Text(text = stringResource(R.string.app_name))
    },
    actions: (@Composable RowScope.() -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement
            .spacedBy(MaterialTheme.dimens.large),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(MaterialTheme.colorScheme.background)
            .padding(
                horizontal = MaterialTheme.dimens.large,
                vertical = MaterialTheme.dimens.small
            )
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.titleLarge,
            LocalContentColor provides MaterialTheme.colorScheme.onBackground
        ) {
            Row(modifier = Modifier.weight(1f)) {
                actions?.invoke(this)
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimens.medium)
            ) {
                title()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShakeToolbarPreview() {
    ShakeTheme {
        ShakeToolbar {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = null
                )
            }
        }
    }
}