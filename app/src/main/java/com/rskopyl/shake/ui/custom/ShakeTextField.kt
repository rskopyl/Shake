package com.rskopyl.shake.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rskopyl.shake.ui.theme.ShakeTheme
import com.rskopyl.shake.ui.theme.dimens

@Composable
fun ShakeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    indicatorSpacing: Dp = MaterialTheme.dimens.medium,
    interactionSource: MutableInteractionSource = remember {
        MutableInteractionSource()
    }
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        decorationBox = @Composable { innerTextField ->
            ShakeTextFieldDecorationBox(
                value = value,
                textStyle = textStyle,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                supportingText = supportingText,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                indicatorSpacing = indicatorSpacing
            )
        }
    )
}

@Composable
private fun ShakeTextFieldDecorationBox(
    value: String,
    textStyle: TextStyle,
    innerTextField: @Composable () -> Unit,
    placeholder: @Composable (() -> Unit)?,
    label: @Composable (() -> Unit)?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    supportingText: @Composable (() -> Unit)?,
    singleLine: Boolean,
    enabled: Boolean,
    isError: Boolean,
    indicatorSpacing: Dp = MaterialTheme.dimens.medium,
    interactionSource: InteractionSource = remember {
        MutableInteractionSource()
    },
    container: @Composable () -> Unit = {
        ShakeUnderlinedContainerBox(
            enabled = enabled,
            isError = isError,
            interactionSource = interactionSource
        )
    }
) {
    TextFieldDefaults.TextFieldDecorationBox(
        value = value,
        visualTransformation = VisualTransformation.None,
        innerTextField = innerTextField,
        placeholder = placeholder?.let {
            {
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.bodyLarge
                        .copy(textAlign = textStyle.textAlign),
                    LocalContentColor provides MaterialTheme.colorScheme.outline,
                    content = it
                )
            }
        },
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        shape = MaterialTheme.shapes.medium,
        singleLine = singleLine,
        enabled = enabled,
        isError = isError,
        interactionSource = interactionSource,
        colors = TextFieldDefaults.textFieldColors(),
        contentPadding = TextFieldDefaults.run {
            val iconPadding = MaterialTheme.dimens.medium
            val start = if (leadingIcon != null) iconPadding else 0.dp
            val end = if (trailingIcon != null) iconPadding else 0.dp
            if (label == null) {
                textFieldWithoutLabelPadding(
                    start = start, end = end, bottom = indicatorSpacing
                )
            } else {
                textFieldWithLabelPadding(
                    start = start, end = end, bottom = indicatorSpacing
                )
            }
        },
        container = container
    )
}

@Composable
private fun ShakeUnderlinedContainerBox(
    enabled: Boolean,
    isError: Boolean,
    interactionSource: InteractionSource = remember {
        MutableInteractionSource()
    }
) {
    val indicatorThickness = MaterialTheme.dimens.stroke
    Box(
        Modifier
            .background(Color.Transparent, MaterialTheme.shapes.extraSmall)
            .indicatorLine(
                enabled, isError, interactionSource,
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                ),
                focusedIndicatorLineThickness = indicatorThickness,
                unfocusedIndicatorLineThickness = indicatorThickness
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun ShakeTextFieldPreview() {
    ShakeTheme {
        ShakeTextField(
            value = "Pina Colada",
            onValueChange = {},
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}