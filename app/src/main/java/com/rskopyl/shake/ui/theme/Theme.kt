package com.rskopyl.shake.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun ShakeTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalDimens provides ShakeDimens(),
        LocalAlpha provides ShakeAlpha()
    ) {
        MaterialTheme(
            colorScheme = ShakeLightColorScheme,
            typography = ShakeTypography,
            shapes = ShakeShapes,
            content = content
        )
    }
}