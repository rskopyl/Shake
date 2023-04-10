package com.rskopyl.shake.repository

import com.rskopyl.shake.data.model.CocktailFilter
import com.rskopyl.shake.data.model.CocktailOverview
import com.rskopyl.shake.util.Resource
import kotlinx.coroutines.flow.Flow

interface CocktailOverviewRepository {

    fun getByNameAndFilter(
        name: String,
        filter: CocktailFilter?
    ): Flow<Resource<List<CocktailOverview>>>
}