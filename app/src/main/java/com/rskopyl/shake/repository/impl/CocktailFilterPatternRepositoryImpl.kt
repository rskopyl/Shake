package com.rskopyl.shake.repository.impl

import com.rskopyl.shake.data.model.CocktailFilter
import com.rskopyl.shake.data.remote.api.CocktailFilterPatternApi
import com.rskopyl.shake.repository.CocktailFilterPatternRepository
import com.rskopyl.shake.repository.impl.mapper.CocktailFilterPatternListMapper
import com.rskopyl.shake.util.Resource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CocktailFilterPatternRepositoryImpl @Inject constructor(
    private val cocktailFilterApi: CocktailFilterPatternApi
) : CocktailFilterPatternRepository {

    override fun getCocktailFilterPatternsByPurpose(
        purpose: CocktailFilter.Purpose
    ): Flow<Resource<Set<String>>> = flow {
        emit(Resource.Loading())
        try {
            coroutineScope {
                val patterns = cocktailFilterApi.run {
                    val dto = when(purpose) {
                        CocktailFilter.Purpose.CATEGORY -> getCategories()
                        CocktailFilter.Purpose.INGREDIENT -> getIngredients()
                        CocktailFilter.Purpose.ALCOHOL -> getAlcohols()
                        CocktailFilter.Purpose.GLASS -> getGlasses()
                    }
                    CocktailFilterPatternListMapper.from(dto)
                }
                emit(Resource.Success(patterns))
            }
        } catch (_: Exception) {
            emit(Resource.Error())
        }
    }
}