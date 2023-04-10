package com.rskopyl.shake.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ShakeDimens(
    val extraSmall: Dp = 6.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 18.dp,
    val extraLarge: Dp = 24.dp,
    val stroke: Dp = 2.dp
)

val LocalDimens = staticCompositionLocalOf { ShakeDimens() }

val MaterialTheme.dimens
    @Composable
    @ReadOnlyComposable
    get() = LocalDimens.current