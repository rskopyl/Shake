package com.rskopyl.shake.ui.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rskopyl.shake.data.model.Cocktail
import com.rskopyl.shake.data.model.CocktailBookmark
import com.rskopyl.shake.data.model.CocktailVisit
import com.rskopyl.shake.repository.CocktailBookmarkRepository
import com.rskopyl.shake.repository.CocktailRepository
import com.rskopyl.shake.repository.CocktailVisitRepository
import com.rskopyl.shake.util.Resource
import com.rskopyl.shake.util.dataOrNull
import com.rskopyl.shake.util.ifSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailsViewModel @AssistedInject constructor(
    @Assisted cocktailId: String,
    private val cocktailRepository: CocktailRepository,
    private val bookmarkRepository: CocktailBookmarkRepository,
    private val visitRepository: CocktailVisitRepository
) : ViewModel() {

    val cocktail: StateFlow<Resource<Cocktail>> = cocktailRepository
        .getById(cocktailId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading())

    val isCocktailBookmarked: StateFlow<Resource<Boolean>> = bookmarkRepository
        .getById(cocktailId)
        .mapLatest { cocktailBookmark ->
            cocktailBookmark.ifSuccess { it != null }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedCocktail = cocktail
                .filterIsInstance<Resource.Success<Cocktail>>()
                .first().data
            val visit = CocktailVisit(loadedCocktail)
            visitRepository.insert(visit)
        }
    }

    fun toggleCocktailBookmark() {
        val cocktail = cocktail.value.dataOrNull() ?: return
        val isBookmarked = isCocktailBookmarked.value.dataOrNull() ?: return
        if (!isBookmarked) {
            val bookmark = CocktailBookmark(cocktail)
            bookmarkRepository.insert(bookmark)
        } else {
            bookmarkRepository.deleteById(cocktail.id)
        }
    }

    fun deleteCocktail() {
        val cocktail = cocktail.value.dataOrNull() ?: return
        cocktailRepository.deleteById(cocktail.id)
    }

    @AssistedFactory
    interface DaggerFactory {

        fun create(cocktailId: String): DetailsViewModel
    }
}