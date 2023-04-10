package com.rskopyl.shake.ui.screen.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rskopyl.shake.R
import com.rskopyl.shake.data.model.CocktailFilter
import com.rskopyl.shake.ui.custom.ShakeAlertDialog
import com.rskopyl.shake.ui.custom.ShakeTextButton
import com.rskopyl.shake.ui.theme.ShakeTheme
import com.rskopyl.shake.ui.theme.dimens
import com.rskopyl.shake.util.Resource
import com.rskopyl.shake.util.dataOrNull
import com.rskopyl.shake.util.ifSuccess

@Composable
fun CocktailFilterDialog(
    initialFilter: CocktailFilter,
    onFilterConfirmed: (CocktailFilter) -> Unit,
    onFilterDismissed: () -> Unit,
    viewModel: CocktailFilterViewModel = hiltViewModel()
) {
    viewModel.loadInitialFilter(initialFilter)
    val availablePatterns by viewModel.availablePatterns.collectAsState()
    val configuredFilter by viewModel.configuredFilter.collectAsState()

    CocktailFilterDialog(
        configuredFilter = configuredFilter,
        availablePatterns = availablePatterns,
        onPatternClick = viewModel::selectPattern,
        onFilterConfirmed = {
            configuredFilter.ifSuccess(onFilterConfirmed)
        },
        onFilterDismissed = onFilterDismissed
    )
}

@Composable
private fun CocktailFilterDialog(
    configuredFilter: Resource<CocktailFilter>,
    availablePatterns: Resource<Set<String>>,
    onPatternClick: (String) -> Unit,
    onFilterConfirmed: () -> Unit,
    onFilterDismissed: () -> Unit,
    patternListState: LazyListState = rememberLazyListState()
) {
    ShakeAlertDialog(
        title = {
            val titleId = configuredFilter.dataOrNull()?.let {
                cocktailFilterPurposeToStringId.getValue(it.purpose)
            }
            Text(text = titleId?.let { stringResource(it) } ?: "")
        },
        confirmButton = {
            ShakeTextButton(onClick = onFilterConfirmed) {
                Text(stringResource(R.string.apply))
            }
        },
        dismissButton = {
            ShakeTextButton(onClick = onFilterDismissed) {
                Text(stringResource(R.string.cancel))
            }
        },
        onDismissRequest = onFilterDismissed
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(MaterialTheme.dimens.small),
            state = patternListState,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (availablePatterns is Resource.Success) {
                val selectedPattern = configuredFilter.dataOrNull()?.pattern
                items(
                    availablePatterns.data.toList(),
                    key = { it }
                ) { pattern ->
                    Text(
                        text = pattern,
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = TextDecoration.Underline
                            .takeIf { pattern == selectedPattern },
                        modifier = Modifier
                            .clickable { onPatternClick(pattern) }
                    )
                }
            }
        }
    }
}

private val cocktailFilterPurposeToStringId = mapOf(
    CocktailFilter.Purpose.CATEGORY to R.string.cocktail_filter_category,
    CocktailFilter.Purpose.INGREDIENT to R.string.cocktail_filter_ingredient,
    CocktailFilter.Purpose.ALCOHOL to R.string.cocktail_filter_alcohol,
    CocktailFilter.Purpose.GLASS to R.string.cocktail_filter_glass,
)

@Preview
@Composable
private fun CocktailFilterDialogPreview() {
    ShakeTheme {
        CocktailFilterDialog(
            configuredFilter = Resource.Success(
                CocktailFilter(
                    purpose = CocktailFilter.Purpose.CATEGORY,
                    pattern = "Ordinary Drinks"
                )
            ),
            availablePatterns = Resource.Success(
                setOf("Shot Drinks", "Ordinary Drinks", "New Era Drinks")
            ),
            onPatternClick = {},
            onFilterConfirmed = {},
            onFilterDismissed = {}
        )
    }
}