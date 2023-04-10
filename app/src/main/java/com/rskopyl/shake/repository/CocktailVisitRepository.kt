package com.rskopyl.shake.repository

import com.rskopyl.shake.data.model.CocktailVisit
import com.rskopyl.shake.util.Resource
import kotlinx.coroutines.flow.Flow

interface CocktailVisitRepository {

    fun getAll(): Flow<Resource<List<CocktailVisit>>>

    fun insert(visit: CocktailVisit)
}