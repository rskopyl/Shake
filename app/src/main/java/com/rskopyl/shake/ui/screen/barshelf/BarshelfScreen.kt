package com.rskopyl.shake.ui.screen.barshelf

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rskopyl.shake.R
import com.rskopyl.shake.data.model.Cocktail
import com.rskopyl.shake.data.model.CocktailBookmark
import com.rskopyl.shake.data.model.CocktailVisit
import com.rskopyl.shake.ui.custom.ShakeTab
import com.rskopyl.shake.ui.custom.ShakeTabRow
import com.rskopyl.shake.ui.custom.ShakeToolbar
import com.rskopyl.shake.ui.custom.mirroring
import com.rskopyl.shake.ui.theme.ShakeTheme
import com.rskopyl.shake.ui.theme.dimens
import com.rskopyl.shake.util.Resource
import kotlinx.datetime.LocalDateTime

enum class BarshelfTab {
    SAVED, HISTORY, CUSTOM
}

sealed interface BarshelfCollection {

    data class Bookmarks(
        val items: Resource<List<CocktailBookmark>>
    ) : BarshelfCollection

    data class SearchHistory(
        val items: Resource<List<CocktailVisit>>
    ) : BarshelfCollection

    data class CustomCocktails(
        val items: Resource<List<Cocktail>>
    ) : BarshelfCollection
}

@Composable
fun BarshelfScreen(
    onNavigateToDetails: (String) -> Unit,
    viewModel: BarshelfViewModel = hiltViewModel()
) {
    val activeBarshelfTab by viewModel.activeBarshelfTab.collectAsState()
    val barshelfCollection by viewModel.barshelfCollection.collectAsState()

    BarshelfScreen(
        activeBarshelfTab = activeBarshelfTab,
        barshelfCollection = barshelfCollection,
        onBarshelfTabClick = viewModel::selectBarshelfTab,
        onCocktailBookmarkClick = { bookmark ->
            onNavigateToDetails(bookmark.cocktailId)
        },
        onCocktailVisitClick = { visit ->
            onNavigateToDetails(visit.cocktailId)
        },
        onCustomCocktailClick = { cocktail ->
            onNavigateToDetails(cocktail.id)
        }
    )
}

@Composable
private fun BarshelfScreen(
    activeBarshelfTab: BarshelfTab,
    barshelfCollection: BarshelfCollection,
    onBarshelfTabClick: (BarshelfTab) -> Unit,
    onCocktailBookmarkClick: (CocktailBookmark) -> Unit,
    onCocktailVisitClick: (CocktailVisit) -> Unit,
    onCustomCocktailClick: (Cocktail) -> Unit,
    cocktailBookmarksListState: LazyListState = rememberLazyListState(),
    cocktailVisitsListState: LazyListState = rememberLazyListState(),
    customCocktailsListState: LazyListState = rememberLazyListState()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ShakeToolbar()
        Spacer(
            modifier = Modifier
                .height(MaterialTheme.dimens.extraLarge)
        )
        ShakeTabRow(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimens.extraLarge)
        ) {
            for (barshelfTab in BarshelfTab.values()) {
                ShakeTab(
                    selected = barshelfTab == activeBarshelfTab,
                    onClick = { onBarshelfTabClick(barshelfTab) }
                ) {
                    val titleId = barshelfTabToStringId.getValue(barshelfTab)
                    Text(stringResource(titleId))
                }
            }
        }
        when (barshelfCollection) {
            is BarshelfCollection.Bookmarks -> {
                CocktailBookmarks(
                    bookmarks = barshelfCollection.items,
                    onBookmarkClick = onCocktailBookmarkClick,
                    listState = cocktailBookmarksListState,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is BarshelfCollection.SearchHistory -> {
                SearchHistory(
                    visits = barshelfCollection.items,
                    onVisitClick = onCocktailVisitClick,
                    listState = cocktailVisitsListState,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is BarshelfCollection.CustomCocktails -> {
                CustomCocktails(
                    cocktails = barshelfCollection.items,
                    onCocktailClick = onCustomCocktailClick,
                    listState = customCocktailsListState,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun CocktailBookmarks(
    bookmarks: Resource<List<CocktailBookmark>>,
    onBookmarkClick: (CocktailBookmark) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    if (bookmarks !is Resource.Success) return
    BarshelfCollection(
        listState = listState,
        modifier = modifier
    ) {
        items(
            bookmarks.data,
            key = { bookmark -> bookmark.cocktailId }
        ) { bookmark ->
            BarshelfCollectionItem(
                image = bookmark.cocktailImage,
                name = bookmark.cocktailName,
                info = stringResource(
                    R.string.cocktail_bookmark_saved_on_value,
                    bookmark.savingDateTime.date.toString()
                ),
                onClick = { onBookmarkClick(bookmark) }
            )
        }
    }
}

@Composable
private fun SearchHistory(
    visits: Resource<List<CocktailVisit>>,
    onVisitClick: (CocktailVisit) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    if (visits !is Resource.Success) return
    BarshelfCollection(
        listState = listState,
        modifier = modifier
    ) {
        items(
            visits.data,
            key = { visit -> visit.cocktailId }
        ) { visit ->
            BarshelfCollectionItem(
                image = visit.cocktailImage,
                name = visit.cocktailName,
                info = stringResource(
                    R.string.cocktail_visit_visited_on_value,
                    visit.visitDateTime.date.toString()
                ),
                onClick = { onVisitClick(visit) }
            )
        }
    }
}

@Composable
private fun CustomCocktails(
    cocktails: Resource<List<Cocktail>>,
    onCocktailClick: (Cocktail) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    if (cocktails !is Resource.Success) return
    BarshelfCollection(
        listState = listState,
        modifier = modifier
    ) {
        items(
            cocktails.data,
            key = { cocktail -> cocktail.id }
        ) { cocktail ->
            BarshelfCollectionItem(
                image = cocktail.image,
                name = cocktail.name,
                info = cocktail.tags.joinToString(),
                onClick = { onCocktailClick(cocktail) }
            )
        }
    }
}

@Composable
private fun BarshelfCollection(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement
            .spacedBy(MaterialTheme.dimens.extraLarge),
        contentPadding = PaddingValues(
            MaterialTheme.dimens.extraLarge
        ),
        state = listState,
        modifier = modifier,
        content = content
    )
}

@Composable
private fun BarshelfCollectionItem(
    image: String, name: String, info: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .mirroring(MaterialTheme.dimens.medium)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = image,
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .aspectRatio(1f)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(MaterialTheme.dimens.medium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = info,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )
        }
    }
}

private val barshelfTabToStringId = mapOf(
    BarshelfTab.SAVED to R.string.barshelf_tab_saved,
    BarshelfTab.HISTORY to R.string.barshelf_tab_history,
    BarshelfTab.CUSTOM to R.string.barshelf_tab_custom
)

@Preview
@Composable
private fun BarshelfScreenPreview() {
    ShakeTheme {
        BarshelfScreen(
            activeBarshelfTab = BarshelfTab.SAVED,
            barshelfCollection = BarshelfCollection.Bookmarks(
                items = Resource.Success(
                    data = listOf(
                        CocktailBookmark(
                            cocktailId = "0",
                            cocktailName = "Pina Colada",
                            cocktailImage = "",
                            savingDateTime = LocalDateTime
                                .parse("2023-03-07T12:00:00")
                        )
                    )
                )
            ),
            onBarshelfTabClick = {},
            onCocktailBookmarkClick = {},
            onCocktailVisitClick = {},
            onCustomCocktailClick = {}
        )
    }
}