package com.rskopyl.shake.ui.custom

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize

fun Modifier.mirroring(
    horizontalInset: Dp,
    verticalInset: Dp = horizontalInset,
    color: Color = Color.Unspecified
): Modifier = composed {
    val rectColor = color.takeOrElse {
        MaterialTheme.colorScheme.secondaryContainer
    }

    return@composed this
        .padding(top = verticalInset, end = horizontalInset)
        .drawBehind {
            translate(
                left = horizontalInset.toPx(),
                top = -verticalInset.toPx()
            ) {
                drawRect(color = rectColor)
            }
        }
}

fun Modifier.shimmer(
    blinkColor: Color = Color.Unspecified,
    containerColor: Color = Color.Transparent
): Modifier = composed {
    val brushBlinkColor = blinkColor.takeOrElse {
        MaterialTheme.colorScheme.secondary
    }
    var size by remember { mutableStateOf(IntSize.Zero) }
    val blinkTransition = rememberInfiniteTransition()
    val blinkStartOffsetX by blinkTransition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000)
        )
    )

    return@composed this
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    containerColor,
                    brushBlinkColor,
                    containerColor
                ),
                start = Offset(x = blinkStartOffsetX, y = 0f),
                end = Offset(
                    x = blinkStartOffsetX + size.width,
                    y = size.height.toFloat()
                )
            )
        )
        .onGloballyPositioned { coordinates ->
            size = coordinates.size
        }
}

fun Modifier.shimmerOn(
    condition: Boolean,
    blinkColor: Color = Color.Unspecified,
    containerColor: Color = Color.Transparent
): Modifier {
    return if (condition) shimmer(blinkColor, containerColor)
    else this
}