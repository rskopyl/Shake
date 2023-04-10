package com.rskopyl.shake.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

data class ShakeAlpha(
    val disabledContainer: Float = 0.35f,
    val disabledContent: Float = 0.35f
)

val LocalAlpha = staticCompositionLocalOf { ShakeAlpha() }

val MaterialTheme.alpha
    @Composable
    @ReadOnlyComposable
    get() = LocalAlpha.current