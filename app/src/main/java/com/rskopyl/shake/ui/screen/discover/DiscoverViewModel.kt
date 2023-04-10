package com.rskopyl.shake.ui.screen.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rskopyl.shake.data.model.CocktailFilter
import com.rskopyl.shake.data.model.CocktailOverview
import com.rskopyl.shake.repository.CocktailOverviewRepository
import com.rskopyl.shake.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val cocktailOverviewRepository: CocktailOverviewRepository
) : ViewModel() {

    private val _cocktailName = MutableStateFlow("")

    private val _cocktailFilters = MutableStateFlow(
        listOf(
            CocktailFilter(CocktailFilter.Purpose.CATEGORY),
            CocktailFilter(CocktailFilter.Purpose.INGREDIENT),
            CocktailFilter(CocktailFilter.Purpose.ALCOHOL),
            CocktailFilter(CocktailFilter.Purpose.GLASS)
        )
    )

    private val _cocktailOverviews =
        MutableStateFlow<Resource<List<CocktailOverview>>>(Resource.Loading())

    val cocktailName: StateFlow<String>
        get() = _cocktailName.asStateFlow()

    val cocktailFilters: StateFlow<List<CocktailFilter>>
        get() = _cocktailFilters.asStateFlow()

    val cocktailOverviews: StateFlow<Resource<List<CocktailOverview>>>
        get() = _cocktailOverviews.asStateFlow()

    init {
        search()
    }

    fun changeCocktailName(name: String) {
        _cocktailName.value = name
        if (name.isEmpty()) search()
    }

    fun applyCocktailFilter(filter: CocktailFilter) {
        _cocktailFilters.update { filters ->
            filters
                .map {
                    if (it.purpose == filter.purpose) filter
                    else it.copy(pattern = null)
                }
                .sortedByDescending { it.pattern != null }
        }
        search()
    }

    fun clearCocktailFilter(filter: CocktailFilter) {
        _cocktailFilters.update { filters ->
            filters
                .map {
                    if (it == filter) it.copy(pattern = null)
                    else it
                }
                .sortedByDescending { it.pattern != null }
        }
        search()
    }

    fun search() {
        viewModelScope.launch(Dispatchers.IO) {
            cocktailOverviewRepository
                .getByNameAndFilter(
                    name = _cocktailName.value,
                    filter = _cocktailFilters.value
                        .singleOrNull { it.pattern != null }
                )
                .collectLatest { overviews ->
                    _cocktailOverviews.value = overviews
                }
        }
    }
}