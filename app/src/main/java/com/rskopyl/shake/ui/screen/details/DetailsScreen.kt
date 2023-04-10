package com.rskopyl.shake.ui.screen.details

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.rskopyl.shake.R
import com.rskopyl.shake.data.model.Cocktail
import com.rskopyl.shake.data.model.CocktailIngredient
import com.rskopyl.shake.ui.custom.ShakeFilterChip
import com.rskopyl.shake.ui.custom.ShakeToolbar
import com.rskopyl.shake.ui.custom.mirroring
import com.rskopyl.shake.ui.MainActivity.ViewModelFactoryEntryPoint
import com.rskopyl.shake.ui.theme.ShakeTheme
import com.rskopyl.shake.ui.theme.dimens
import com.rskopyl.shake.util.Resource
import com.rskopyl.shake.util.ifSuccess
import com.rskopyl.shake.util.invoke
import dagger.hilt.EntryPoints

@Composable
fun DetailsScreen(
    cocktailId: String,
    onNavigateBack: () -> Unit,
    viewModel: DetailsViewModel = viewModel(
        factory = ViewModelProvider.Factory { context ->
            EntryPoints
                .get(context, ViewModelFactoryEntryPoint::class.java)
                .detailsDaggerFactory
                .create(cocktailId)
        }
    )
) {
    val cocktail by viewModel.cocktail.collectAsState()
    val isCocktailBookmarked by viewModel.isCocktailBookmarked.collectAsState()

    DetailsScreen(
        cocktail = cocktail,
        isCocktailBookmarked = isCocktailBookmarked,
        onDeleteCocktailClick = {
            viewModel.deleteCocktail()
            onNavigateBack()
        },
        onCocktailBookmarkClick = viewModel::toggleCocktailBookmark,
        onBackActionClick = onNavigateBack
    )
}

@Composable
private fun DetailsScreen(
    cocktail: Resource<Cocktail>,
    isCocktailBookmarked: Resource<Boolean>,
    onCocktailBookmarkClick: () -> Unit,
    onDeleteCocktailClick: () -> Unit,
    onBackActionClick: () -> Unit,
    scrollState: ScrollState = rememberScrollState()
) {
    Scaffold(
        topBar = {
            DetailsShakeToolbar(
                origin = cocktail.ifSuccess { it.origin },
                isCocktailBookmarked = isCocktailBookmarked,
                onCocktailBookmarkClick = onCocktailBookmarkClick,
                onDeleteCocktailClick = onDeleteCocktailClick,
                onBackActionClick = onBackActionClick
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = MaterialTheme.dimens.extraLarge)
                .mirroring(
                    horizontalInset = MaterialTheme.dimens.medium,
                    verticalInset = 0.dp
                )
        ) {
            if (cocktail is Resource.Success) {
                Column(
                    verticalArrangement = Arrangement
                        .spacedBy(MaterialTheme.dimens.medium),
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(bottom = MaterialTheme.dimens.extraLarge)
                ) {
                    CocktailOverview(
                        image = cocktail.data.image,
                        name = cocktail.data.name,
                        modifier = Modifier.fillMaxWidth()
                    )
                    CocktailTags(
                        tags = cocktail.data.tags,
                        modifier = Modifier.fillMaxWidth()
                    )
                    CocktailIngredients(
                        ingredients = cocktail.data.ingredients,
                        modifier = Modifier.fillMaxWidth()
                    )
                    CocktailInstructions(
                        instructions = cocktail.data.instructions,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else if (cocktail is Resource.Error) {
                Text(
                    stringResource(R.string.cocktail_not_found),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun DetailsShakeToolbar(
    origin: Resource<Cocktail.Origin>,
    isCocktailBookmarked: Resource<Boolean>,
    onCocktailBookmarkClick: () -> Unit,
    onDeleteCocktailClick: () -> Unit,
    onBackActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ShakeToolbar(modifier = modifier) {
        IconButton(onClick = onBackActionClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }
        if (isCocktailBookmarked is Resource.Success) {
            IconButton(onClick = onCocktailBookmarkClick) {
                Icon(
                    painter = painterResource(
                        if (isCocktailBookmarked.data) {
                            R.drawable.ic_bookmark_filled
                        } else {
                            R.drawable.ic_bookmark_add_outlined
                        }
                    ),
                    contentDescription =
                    stringResource(R.string.cocktail_bookmark)
                )
            }
        }
        if (origin is Resource.Success) {
            if (origin.data == Cocktail.Origin.CUSTOM) {
                IconButton(onClick = onDeleteCocktailClick) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription =
                        stringResource(R.string.delete)
                    )
                }
            }
        }
    }
}

@Composable
private fun CocktailOverview(
    image: String,
    name: String,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier
                    .padding(MaterialTheme.dimens.large)
            )
        }
    }
}

@Composable
private fun CocktailTags(
    tags: Set<String>,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        FlowRow(
            mainAxisSpacing = MaterialTheme.dimens.small,
            crossAxisSpacing = MaterialTheme.dimens.small,
            modifier = Modifier
                .padding(MaterialTheme.dimens.medium)
        ) {
            for (tag in tags) {
                ShakeFilterChip(
                    selected = false,
                    label = { Text(tag) }
                )
            }
        }
    }
}

@Composable
private fun CocktailIngredients(
    ingredients: List<CocktailIngredient>,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(MaterialTheme.dimens.small),
            modifier = Modifier
                .padding(MaterialTheme.dimens.medium)
        ) {
            Text(
                text = stringResource(R.string.cocktail_ingredients),
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            for ((index, ingredient) in ingredients.withIndex()) {
                CocktailIngredient(
                    ingredient = ingredient,
                    position = index.inc(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun CocktailIngredient(
    ingredient: CocktailIngredient,
    position: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement
            .spacedBy(MaterialTheme.dimens.small),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = MaterialTheme.dimens.medium)
        ) {
            Text(
                text = position.toString(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Text(
            text = if (ingredient.measure != null) {
                stringResource(
                    R.string.cocktail_ingredient_value,
                    ingredient.name, ingredient.measure
                )
            } else {
                ingredient.name
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun CocktailInstructions(
    instructions: String,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(MaterialTheme.dimens.medium),
            modifier = Modifier
                .padding(MaterialTheme.dimens.medium)
        ) {
            Text(
                text = stringResource(R.string.cocktail_instructions),
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = instructions,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun DetailsScreenPreview() {
    ShakeTheme {
        DetailsScreen(
            cocktail = Resource.Success(
                Cocktail(
                    id = "0",
                    name = "Pina Colada",
                    tags = setOf("IBA", "Contemporary classics"),
                    ingredients = listOf(
                        CocktailIngredient("White Rum", "50ml"),
                    ),
                    instructions = "Mix with crushed ice in blender until smooth.",
                    image = ""
                )
            ),
            isCocktailBookmarked = Resource.Success(data = false),
            onCocktailBookmarkClick = {},
            onDeleteCocktailClick = {},
            onBackActionClick = {}
        )
    }
}