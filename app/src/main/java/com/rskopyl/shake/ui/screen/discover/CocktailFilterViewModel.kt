package com.rskopyl.shake.ui.screen.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rskopyl.shake.data.model.CocktailFilter
import com.rskopyl.shake.repository.CocktailFilterPatternRepository
import com.rskopyl.shake.util.Resource
import com.rskopyl.shake.util.ifSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CocktailFilterViewModel @Inject constructor(
    private val patternRepository: CocktailFilterPatternRepository
) : ViewModel() {

    private val initialFilter =
        MutableStateFlow<Resource<CocktailFilter>>(Resource.Loading())

    private val _selectedPattern = MutableStateFlow<String?>(null)

    val configuredFilter: StateFlow<Resource<CocktailFilter>> =
        combine(initialFilter, _selectedPattern) { filter, pattern ->
            filter.ifSuccess {
                it.copy(pattern = pattern)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading())

    val availablePatterns: StateFlow<Resource<Set<String>>> = initialFilter
        .filterIsInstance<Resource.Success<CocktailFilter>>()
        .flatMapLatest { filter ->
            val purpose = filter.data.purpose
            patternRepository.getCocktailFilterPatternsByPurpose(purpose)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading())

    fun loadInitialFilter(filter: CocktailFilter) {
        initialFilter.value = Resource.Success(filter)
    }

    fun selectPattern(pattern: String) {
        _selectedPattern.value = pattern
    }
}