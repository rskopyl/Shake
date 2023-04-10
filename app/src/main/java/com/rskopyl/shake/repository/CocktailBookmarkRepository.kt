package com.rskopyl.shake.repository

import com.rskopyl.shake.data.model.CocktailBookmark
import com.rskopyl.shake.util.Resource
import kotlinx.coroutines.flow.Flow

interface CocktailBookmarkRepository {

    fun getAll(): Flow<Resource<List<CocktailBookmark>>>

    fun getById(id: String): Flow<Resource<CocktailBookmark?>>

    fun insert(bookmark: CocktailBookmark)

    fun deleteById(id: String)
}