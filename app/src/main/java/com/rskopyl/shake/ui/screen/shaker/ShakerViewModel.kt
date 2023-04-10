package com.rskopyl.shake.ui.screen.shaker

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.rskopyl.shake.data.model.Cocktail
import com.rskopyl.shake.data.model.CocktailIngredient
import com.rskopyl.shake.repository.CocktailRepository
import com.rskopyl.shake.util.Resource
import com.rskopyl.shake.util.dataOrNull
import com.rskopyl.shake.util.updateIfSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ShakerViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository
) : ViewModel() {

    private val _cocktail =
        MutableStateFlow<Resource<Cocktail>>(Resource.Success(EMPTY_COCKTAIL))

    private val _violations =
        MutableStateFlow<Set<ShakerViolation>>(emptySet())

    val cocktail: StateFlow<Resource<Cocktail>>
        get() = _cocktail.asStateFlow()

    val violations: StateFlow<Set<ShakerViolation>>
        get() = _violations.asStateFlow()

    fun changeCocktailImage(uri: Uri?) {
        if (uri != null) {
            _cocktail.updateIfSuccess { cocktail ->
                cocktail.copy(image = uri.toString())
            }
        }
    }

    fun changeCocktailName(name: String) {
        _cocktail.updateIfSuccess { cocktail ->
            cocktail.copy(name = name)
        }
    }

    fun addCocktailTag(tag: String) {
        _cocktail.updateIfSuccess { cocktail ->
            cocktail.copy(tags = cocktail.tags + tag)
        }
    }

    fun removeCocktailTag(tag: String) {
        _cocktail.updateIfSuccess { cocktail ->
            cocktail.copy(tags = cocktail.tags - tag)
        }
    }

    fun addCocktailIngredient(ingredient: CocktailIngredient) {
        _cocktail.updateIfSuccess { cocktail ->
            cocktail.copy(ingredients = cocktail.ingredients + ingredient)
        }
    }

    fun removeCocktailIngredient(ingredient: CocktailIngredient) {
        _cocktail.updateIfSuccess { cocktail ->
            cocktail.copy(ingredients = cocktail.ingredients - ingredient)
        }
    }

    fun changeCocktailInstructions(instructions: String) {
        _cocktail.updateIfSuccess { cocktail ->
            cocktail.copy(instructions = instructions)
        }
    }

    fun saveCocktail() {
        val cocktail = cocktail.value.dataOrNull() ?: return
        val violations = buildSet {
            if (cocktail.name.isBlank()) add(ShakerViolation.BLANK_NAME)
            if (cocktail.image.isBlank()) add(ShakerViolation.EMPTY_IMAGE)
        }
        if (violations.isEmpty()) {
            cocktailRepository.upsert(cocktail)
            _cocktail.value = Resource.Success(EMPTY_COCKTAIL)
        } else {
            _violations.value = violations
        }
    }

    private companion object {

        val EMPTY_COCKTAIL = Cocktail(
            name = "",
            tags = emptySet(),
            ingredients = emptyList(),
            instructions = "",
            image = "",
            origin = Cocktail.Origin.CUSTOM
        )
    }
}