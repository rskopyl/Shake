package com.rskopyl.shake.repository

import com.rskopyl.shake.data.model.Cocktail
import com.rskopyl.shake.util.Resource
import kotlinx.coroutines.flow.Flow

interface CocktailRepository {

    fun getById(id: String): Flow<Resource<Cocktail>>

    fun getCustom(): Flow<Resource<List<Cocktail>>>

    fun upsert(cocktail: Cocktail)

    fun deleteById(id: String)
}