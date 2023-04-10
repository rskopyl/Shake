package com.rskopyl.shake.data.remote.api

import com.rskopyl.shake.data.remote.dto.AlcoholFilterPatternListDto
import com.rskopyl.shake.data.remote.dto.CategoryFilterPatternListDto
import com.rskopyl.shake.data.remote.dto.GlassFilterPatternListDto
import com.rskopyl.shake.data.remote.dto.IngredientFilterPatternListDto
import retrofit2.http.GET

interface CocktailFilterPatternApi {

    @GET("list.php?c=list")
    suspend fun getCategories(): CategoryFilterPatternListDto

    @GET("list.php?i=list")
    suspend fun getIngredients(): IngredientFilterPatternListDto

    @GET("list.php?a=list")
    suspend fun getAlcohols(): AlcoholFilterPatternListDto

    @GET("list.php?g=list")
    suspend fun getGlasses(): GlassFilterPatternListDto
}