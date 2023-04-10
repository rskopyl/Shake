package com.rskopyl.shake.ui.screen.shaker

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.rskopyl.shake.R
import com.rskopyl.shake.data.model.Cocktail
import com.rskopyl.shake.data.model.CocktailIngredient
import com.rskopyl.shake.ui.custom.*
import com.rskopyl.shake.ui.theme.ShakeTheme
import com.rskopyl.shake.ui.theme.alpha
import com.rskopyl.shake.ui.theme.dimens
import com.rskopyl.shake.util.Resource

enum class ShakerViolation {
    BLANK_NAME, EMPTY_IMAGE
}

@Composable
fun ShakerScreen(
    viewModel: ShakerViewModel = hiltViewModel()
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = PickVisualMedia(),
        onResult = viewModel::changeCocktailImage
    )

    val cocktail by viewModel.cocktail.collectAsState()
    val violations by viewModel.violations.collectAsState()
    var isAddingNewTag by rememberSaveable {
        mutableStateOf(false)
    }
    var isAddingNewIngredient by rememberSaveable {
        mutableStateOf(false)
    }

    if (isAddingNewTag) {
        ShakerTagDialog(
            onNewTagConfirm = { newTag ->
                viewModel.addCocktailTag(newTag)
                isAddingNewTag = false
            },
            onNewTagDismiss = { isAddingNewTag = false }
        )
    }
    if (isAddingNewIngredient) {
        ShakerIngredientDialog(
            onNewIngredientConfirm = { newIngredient ->
                viewModel.addCocktailIngredient(newIngredient)
                isAddingNewIngredient = false
            },
            onNewIngredientDismiss = { isAddingNewIngredient = false }
        )
    }

    ShakerScreen(
        cocktail = cocktail,
        violations = violations,
        onPickCocktailImageRequest = {
            imagePickerLauncher.launch(
                PickVisualMediaRequest(PickVisualMedia.ImageOnly)
            )
        },
        onCocktailNameChange = viewModel::changeCocktailName,
        onAddCocktailTagRequest = { isAddingNewTag = true },
        onRemoveCocktailTag = viewModel::removeCocktailTag,
        onAddCocktailIngredientRequest = { isAddingNewIngredient = true },
        onRemoveCocktailIngredient = viewModel::removeCocktailIngredient,
        onCocktailInstructionsChange = viewModel::changeCocktailInstructions,
        onSaveCocktail = viewModel::saveCocktail
    )
}

@Composable
private fun ShakerScreen(
    cocktail: Resource<Cocktail>,
    violations: Set<ShakerViolation>,
    onPickCocktailImageRequest: () -> Unit,
    onCocktailNameChange: (String) -> Unit,
    onAddCocktailTagRequest: () -> Unit,
    onRemoveCocktailTag: (String) -> Unit,
    onAddCocktailIngredientRequest: () -> Unit,
    onRemoveCocktailIngredient: (CocktailIngredient) -> Unit,
    onCocktailInstructionsChange: (String) -> Unit,
    onSaveCocktail: () -> Unit,
    scrollState: ScrollState = rememberScrollState()
) {
    Scaffold(
        topBar = { ShakeToolbar() },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement
                .spacedBy(MaterialTheme.dimens.medium),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(MaterialTheme.dimens.extraLarge)
                .mirroring(MaterialTheme.dimens.medium)
        ) {
            if (cocktail is Resource.Success) {
                CocktailOverview(
                    image = cocktail.data.image,
                    name = cocktail.data.name,
                    onPickImageRequest = onPickCocktailImageRequest,
                    onNameChange = onCocktailNameChange,
                    modifier = Modifier.fillMaxWidth()
                )
                CocktailTags(
                    tags = cocktail.data.tags,
                    onAddTagRequest = onAddCocktailTagRequest,
                    onTagRemove = onRemoveCocktailTag,
                    modifier = Modifier.fillMaxWidth()
                )
                CocktailIngredients(
                    ingredients = cocktail.data.ingredients,
                    onRemoveIngredient = onRemoveCocktailIngredient,
                    onAddIngredientRequest = onAddCocktailIngredientRequest,
                    modifier = Modifier.fillMaxWidth()
                )
                CocktailInstructions(
                    instructions = cocktail.data.instructions,
                    onInstructionsChange = onCocktailInstructionsChange,
                    modifier = Modifier.fillMaxWidth()
                )
                ShakeButton(
                    onClick = onSaveCocktail,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        stringResource(R.string.save),
                        modifier = Modifier
                            .padding(vertical = MaterialTheme.dimens.extraSmall)
                    )
                }
            }
        }
    }
    if (violations.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            stringResource(
                shakerViolationToStringId.getValue(violations.first())
            ),
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
private fun CocktailOverview(
    image: String,
    name: String,
    onPickImageRequest: () -> Unit,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clickable(onClick = onPickImageRequest)
        ) {
            if (image.isEmpty()) {
                Text(
                    stringResource(R.string.shaker_image_placeholder),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            } else {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        ShakeTextField(
            value = name,
            onValueChange = onNameChange,
            placeholder = {
                Text(
                    "Cocktail Name",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                )
            },
            textStyle = MaterialTheme.typography.titleLarge
                .copy(textAlign = TextAlign.Center),
            singleLine = true,
            modifier = Modifier
                .padding(MaterialTheme.dimens.large)
        )
    }
}

@Composable
private fun CocktailTags(
    tags: Set<String>,
    onAddTagRequest: () -> Unit,
    onTagRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        mainAxisSpacing = MaterialTheme.dimens.small,
        crossAxisSpacing = MaterialTheme.dimens.small,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(MaterialTheme.dimens.medium)
    ) {
        for (tag in tags) {
            ShakeFilterChip(
                selected = true,
                label = { Text(tag) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { onTagRemove(tag) }
                    )
                }
            )
        }
        ShakeFilterChip(
            selected = true,
            label = { Text(stringResource(R.string.shaker_new_tag)) },
            enabled = false,
            onClick = onAddTagRequest
        )
    }
}

@Composable
private fun CocktailIngredients(
    ingredients: List<CocktailIngredient>,
    onAddIngredientRequest: () -> Unit,
    onRemoveIngredient: (CocktailIngredient) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement
            .spacedBy(MaterialTheme.dimens.small),
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(MaterialTheme.dimens.medium)
    ) {
        Text(
            text = stringResource(R.string.cocktail_ingredients),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        for ((index, ingredient) in ingredients.withIndex()) {
            CocktailIngredient(
                ingredient = ingredient,
                position = index.inc(),
                onIngredientRemove = { onRemoveIngredient(ingredient) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        CocktailIngredient(
            ingredient = CocktailIngredient(
                name = stringResource(R.string.shaker_new_ingredient),
                measure = null
            ),
            position = ingredients.size.inc(),
            onIngredientRemove = null,
            modifier = Modifier
                .clickable(onClick = onAddIngredientRequest)
                .alpha(MaterialTheme.alpha.disabledContainer)
        )
    }
}

@Composable
private fun CocktailIngredient(
    ingredient: CocktailIngredient,
    position: Int,
    onIngredientRemove: (() -> Unit)?,
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
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        if (onIngredientRemove != null) {
            IconButton(onClick = onIngredientRemove) {
                Icon(Icons.Default.Close, contentDescription = null)
            }
        }
    }
}

@Composable
private fun CocktailInstructions(
    instructions: String,
    onInstructionsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement
            .spacedBy(MaterialTheme.dimens.small),
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(MaterialTheme.dimens.medium)
    ) {
        Text(
            text = stringResource(R.string.cocktail_instructions),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        ShakeTextField(
            value = instructions,
            onValueChange = onInstructionsChange,
            placeholder = {
                Text(
                    stringResource(R.string.shaker_instructions_content),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private val shakerViolationToStringId = mapOf(
    ShakerViolation.BLANK_NAME to R.string.shaker_name_blank_violation,
    ShakerViolation.EMPTY_IMAGE to R.string.shaker_image_empty_violation
)

@Preview
@Composable
private fun ShakerScreenPreview() {
    ShakeTheme {
        ShakerScreen(
            cocktail = Resource.Success(
                data = Cocktail(
                    id = "",
                    name = "Milk Colada",
                    tags = setOf("Contemporary Classics"),
                    ingredients = listOf(
                        CocktailIngredient(
                            name = "Coconut Rum",
                            measure = "25 ml"
                        ),
                    ),
                    instructions = "",
                    image = ""
                )
            ),
            violations = emptySet(),
            onPickCocktailImageRequest = {},
            onCocktailNameChange = {},
            onAddCocktailTagRequest = {},
            onRemoveCocktailTag = {},
            onRemoveCocktailIngredient = {},
            onAddCocktailIngredientRequest = {},
            onCocktailInstructionsChange = {},
            onSaveCocktail = {}
        )
    }
}