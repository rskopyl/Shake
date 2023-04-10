package com.rskopyl.shake.repository

import com.rskopyl.shake.data.model.CocktailFilter
import com.rskopyl.shake.util.Resource
import kotlinx.coroutines.flow.Flow

interface CocktailFilterPatternRepository {

    fun getCocktailFilterPatternsByPurpose(
        purpose: CocktailFilter.Purpose
    ): Flow<Resource<Set<String>>>
}