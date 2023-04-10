package com.rskopyl.shake.data.remote.api

import com.rskopyl.shake.data.remote.dto.CocktailOverviewListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailOverviewApi {

    @GET("search.php")
    suspend fun getByName(
        @Query("s") name: String
    ): CocktailOverviewListDto

    @GET("filter.php")
    suspend fun getByCategory(
        @Query("c") category: String
    ): CocktailOverviewListDto

    @GET("filter.php")
    suspend fun getByIngredient(
        @Query("i") ingredient: String
    ): CocktailOverviewListDto

    @GET("filter.php")
    suspend fun getByAlcohol(
        @Query("a") alcohol: String
    ): CocktailOverviewListDto

    @GET("filter.php")
    suspend fun getByGlass(
        @Query("g") glass: String
    ): CocktailOverviewListDto
}