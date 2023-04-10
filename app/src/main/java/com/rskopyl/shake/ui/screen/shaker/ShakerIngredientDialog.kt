package com.rskopyl.shake.ui.screen.shaker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.rskopyl.shake.R
import com.rskopyl.shake.data.model.CocktailIngredient
import com.rskopyl.shake.ui.custom.ShakeAlertDialog
import com.rskopyl.shake.ui.custom.ShakeTextButton
import com.rskopyl.shake.ui.custom.ShakeTextField
import com.rskopyl.shake.ui.theme.ShakeTheme
import com.rskopyl.shake.ui.theme.dimens

@Composable
fun ShakerIngredientDialog(
    onNewIngredientConfirm: (CocktailIngredient) -> Unit,
    onNewIngredientDismiss: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var measure by rememberSaveable { mutableStateOf("") }

    ShakerIngredientDialog(
        name = name,
        measure = measure,
        onNameChange = { text -> name = text },
        onMeasureChange = { text -> measure = text },
        onNewIngredientConfirm = {
            val ingredient = CocktailIngredient(name, measure)
            onNewIngredientConfirm(ingredient)
        },
        onNewIngredientDismiss = onNewIngredientDismiss
    )
}

@Composable
private fun ShakerIngredientDialog(
    name: String,
    measure: String,
    onNameChange: (String) -> Unit,
    onMeasureChange: (String) -> Unit,
    onNewIngredientConfirm: () -> Unit,
    onNewIngredientDismiss: () -> Unit
) {
    ShakeAlertDialog(
        title = {
            Text(stringResource(R.string.shaker_new_ingredient))
        },
        confirmButton = {
            ShakeTextButton(onClick = onNewIngredientConfirm) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            ShakeTextButton(onClick = onNewIngredientDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        onDismissRequest = onNewIngredientDismiss
    ) {
        Row(
            horizontalArrangement = Arrangement
                .spacedBy(MaterialTheme.dimens.medium),
            modifier = Modifier.fillMaxWidth()
        ) {
            ShakeTextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = {
                    Text(
                        stringResource(R.string.shaker_ingredient_placeholder),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                textStyle = MaterialTheme.typography.bodyLarge
                    .copy(textAlign = TextAlign.Center),
                maxLines = 1,
                singleLine = true,
                modifier = Modifier.weight(2f)
            )
            ShakeTextField(
                value = measure,
                onValueChange = onMeasureChange,
                placeholder = {
                    Text(
                        stringResource(R.string.shaker_measure_placeholder),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                textStyle = MaterialTheme.typography.bodyLarge
                    .copy(textAlign = TextAlign.Center),
                maxLines = 1,
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
private fun ShakerIngredientDialogPreview() {
    ShakeTheme {
        ShakerIngredientDialog(
            name = "Coconut Rum",
            measure = "25 ml",
            onNameChange = {},
            onMeasureChange = {},
            onNewIngredientConfirm = {},
            onNewIngredientDismiss = {}
        )
    }
}