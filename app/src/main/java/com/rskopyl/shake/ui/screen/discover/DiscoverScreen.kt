package com.rskopyl.shake.ui.screen.discover

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rskopyl.shake.R
import com.rskopyl.shake.data.model.CocktailFilter
import com.rskopyl.shake.data.model.CocktailOverview
import com.rskopyl.shake.ui.custom.*
import com.rskopyl.shake.ui.theme.ShakeTheme
import com.rskopyl.shake.ui.theme.dimens
import com.rskopyl.shake.util.Resource
import com.rskopyl.shake.util.dataOrNull

@Composable
fun DiscoverScreen(
    onNavigateToDetails: (String) -> Unit,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    val cocktailName by viewModel.cocktailName.collectAsState()
    val cocktailFilters by viewModel.cocktailFilters.collectAsState()
    val cocktailOverviews by viewModel.cocktailOverviews.collectAsState()

    var configuredCocktailFilter by rememberSaveable {
        mutableStateOf<CocktailFilter?>(null)
    }

    configuredCocktailFilter?.let { configuredFilter ->
        CocktailFilterDialog(
            initialFilter = configuredFilter,
            onFilterConfirmed = { newFilter ->
                viewModel.applyCocktailFilter(newFilter)
                configuredCocktailFilter = null
            },
            onFilterDismissed = { configuredCocktailFilter = null }
        )
    }

    DiscoverScreen(
        cocktailName = cocktailName,
        cocktailFilters = cocktailFilters,
        cocktailOverviews = cocktailOverviews,
        onCocktailNameChange = viewModel::changeCocktailName,
        onCocktailFilterClick = { filter ->
            configuredCocktailFilter = filter
        },
        onClearCocktailFilter = viewModel::clearCocktailFilter,
        onCocktailOverviewClick = { overview ->
            onNavigateToDetails(overview.id)
        },
        onSearch = viewModel::search
    )
}

@Composable
private fun DiscoverScreen(
    cocktailName: String,
    cocktailFilters: List<CocktailFilter>,
    cocktailOverviews: Resource<List<CocktailOverview>>,
    onCocktailNameChange: (String) -> Unit,
    onCocktailFilterClick: (CocktailFilter) -> Unit,
    onClearCocktailFilter: (CocktailFilter) -> Unit,
    onCocktailOverviewClick: (CocktailOverview) -> Unit,
    onSearch: () -> Unit,
    cocktailFiltersScrollState: ScrollState = rememberScrollState(),
    cocktailOverviewsListState: LazyListState = rememberLazyListState()
) {
    Scaffold(
        topBar = { ShakeToolbar() },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement
                .spacedBy(MaterialTheme.dimens.extraLarge),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val contentPadding = MaterialTheme.dimens.extraLarge
            Search(
                cocktailName = cocktailName,
                onCocktailNameChange = onCocktailNameChange,
                onSearch = onSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = contentPadding)
            )
            CocktailFilters(
                cocktailFilters = cocktailFilters,
                onCocktailFilterClick = onCocktailFilterClick,
                onCocktailFilterClear = onClearCocktailFilter,
                scrollState = cocktailFiltersScrollState,
                modifier = Modifier.fillMaxWidth()
            )
            CocktailOverviews(
                overviews = cocktailOverviews,
                onOverviewClick = onCocktailOverviewClick,
                listState = cocktailOverviewsListState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = contentPadding)
            )
        }
    }
}

@Composable
private fun Search(
    cocktailName: String,
    onCocktailNameChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    ShakeTextField(
        value = cocktailName,
        onValueChange = onCocktailNameChange,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        ),
        placeholder = {
            Text(stringResource(R.string.cocktail_search_placeholder))
        },
        trailingIcon = {
            val focusManager = LocalFocusManager.current
            IconButton(
                onClick = {
                    onSearch()
                    focusManager.clearFocus()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.cocktail_search),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        singleLine = true,
        modifier = modifier
    )
}

@Composable
private fun CocktailFilters(
    cocktailFilters: List<CocktailFilter>,
    onCocktailFilterClick: (CocktailFilter) -> Unit,
    onCocktailFilterClear: (CocktailFilter) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState()
) {
    Row(
        horizontalArrangement = Arrangement
            .spacedBy(MaterialTheme.dimens.medium),
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(horizontal = MaterialTheme.dimens.extraLarge)
    ) {
        for (filter in cocktailFilters) {
            val label = stringResource(
                cocktailFilterPurposeToStringId.getValue(filter.purpose),
                filter.pattern ?: stringResource(R.string.cocktail_pattern_any)
            )
            val isNotClear = filter.pattern != null
            ShakeFilterChip(
                selected = isNotClear,
                label = { Text(label) },
                onClick = { onCocktailFilterClick(filter) },
                trailingIcon = if (isNotClear) {
                    {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(
                                R.string.cocktail_filter_clear
                            ),
                            modifier = Modifier
                                .clickable { onCocktailFilterClear(filter) }
                        )
                    }
                } else null
            )
        }
    }
}

@Composable
private fun CocktailOverviews(
    overviews: Resource<List<CocktailOverview>>,
    onOverviewClick: (CocktailOverview) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        verticalArrangement = Arrangement
            .spacedBy(MaterialTheme.dimens.large),
        contentPadding = PaddingValues(
            bottom = MaterialTheme.dimens.extraLarge
        ),
        state = listState,
        modifier = modifier
    ) {
        if (overviews is Resource.Success) {
            items(overviews.data, key = { it.id }) { overview ->
                CocktailOverview(
                    overview = Resource.Success(overview),
                    onClick = { onOverviewClick(overview) }
                )
            }
        } else if (overviews is Resource.Loading) {
            items(count = 5) {
                CocktailOverview(
                    overview = Resource.Loading(),
                    onClick = {}
                )
            }
        }
    }
}

@Composable
private fun CocktailOverview(
    overview: Resource<CocktailOverview>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .height(IntrinsicSize.Min)
            .mirroring(MaterialTheme.dimens.medium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .shimmerOn(overview is Resource.Loading)
        ) {
            Text(
                text = overview.dataOrNull()?.name ?: "",
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .wrapContentSize(Alignment.Center)
                    .padding(MaterialTheme.dimens.extraLarge)
            )
            AsyncImage(
                model = overview.dataOrNull()?.image,
                contentDescription = overview.dataOrNull()?.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
        }
    }
}

private val cocktailFilterPurposeToStringId = mapOf(
    CocktailFilter.Purpose.CATEGORY to R.string.cocktail_filter_category_value,
    CocktailFilter.Purpose.INGREDIENT to R.string.cocktail_filter_ingredient_value,
    CocktailFilter.Purpose.ALCOHOL to R.string.cocktail_filter_alcohol_value,
    CocktailFilter.Purpose.GLASS to R.string.cocktail_filter_glass_value
)

@Preview
@Composable
private fun DiscoverScreenPreview() {
    ShakeTheme {
        DiscoverScreen(
            cocktailName = "Pina Colada",
            cocktailFilters = listOf(
                CocktailFilter(
                    purpose = CocktailFilter.Purpose.INGREDIENT,
                    pattern = "Tequila"
                ),
                CocktailFilter(CocktailFilter.Purpose.CATEGORY),
                CocktailFilter(CocktailFilter.Purpose.ALCOHOL),
                CocktailFilter(CocktailFilter.Purpose.GLASS)
            ),
            cocktailOverviews = Resource.Success(
                data = listOf(
                    CocktailOverview(id = "", name = "Pina Colada", image = "")
                )
            ),
            onCocktailNameChange = {},
            onCocktailFilterClick = {},
            onClearCocktailFilter = {},
            onCocktailOverviewClick = {},
            onSearch = {}
        )
    }
}