package com.rskopyl.shake.data.remote.api

import com.rskopyl.shake.data.remote.dto.CocktailListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {

    @GET("lookup.php")
    suspend fun getById(
        @Query("i") id: String
    ): CocktailListDto
}