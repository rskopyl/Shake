package com.rskopyl.shake.ui.screen.barshelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rskopyl.shake.repository.CocktailBookmarkRepository
import com.rskopyl.shake.repository.CocktailRepository
import com.rskopyl.shake.repository.CocktailVisitRepository
import com.rskopyl.shake.util.Resource
import com.rskopyl.shake.util.ifSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class BarshelfViewModel @Inject constructor(
    cocktailBookmarkRepository: CocktailBookmarkRepository,
    cocktailVisitRepository: CocktailVisitRepository,
    cocktailRepository: CocktailRepository
) : ViewModel() {

    private val _activeBarshelfTab = MutableStateFlow(BarshelfTab.SAVED)

    private val barshelfBookmarks = cocktailBookmarkRepository
        .getAll()
        .mapLatest { bookmarks ->
            bookmarks.ifSuccess { data ->
                data.sortedByDescending { it.savingDateTime }
            }
        }
        .mapLatest(BarshelfCollection::Bookmarks)

    private val barshelfSearchHistory = cocktailVisitRepository
        .getAll()
        .mapLatest { visits ->
            visits.ifSuccess { data ->
                data.sortedByDescending { it.visitDateTime }
            }
        }
        .mapLatest(BarshelfCollection::SearchHistory)

    private val barshelfCustomCocktails = cocktailRepository
        .getCustom()
        .mapLatest { cocktails ->
            cocktails.ifSuccess { data ->
                data.sortedBy { it.name }
            }
        }
        .mapLatest(BarshelfCollection::CustomCocktails)

    val activeBarshelfTab: StateFlow<BarshelfTab>
        get() = _activeBarshelfTab.asStateFlow()

    val barshelfCollection: StateFlow<BarshelfCollection> = _activeBarshelfTab
        .flatMapLatest { barshelfTab ->
            when (barshelfTab) {
                BarshelfTab.SAVED -> barshelfBookmarks
                BarshelfTab.HISTORY -> barshelfSearchHistory
                BarshelfTab.CUSTOM -> barshelfCustomCocktails
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            BarshelfCollection.Bookmarks(Resource.Loading())
        )

    fun selectBarshelfTab(tab: BarshelfTab) {
        _activeBarshelfTab.value = tab
    }
}