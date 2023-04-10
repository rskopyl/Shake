package com.rskopyl.shake.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CocktailOverviewDto(
    @Json(name = "idDrink")
    val id: String,
    @Json(name = "strDrink")
    val name: String,
    @Json(name = "strDrinkThumb")
    val image: String
)

@JsonClass(generateAdapter = true)
data class CocktailOverviewListDto(
    @Json(name = "drinks")
    val overviews: List<CocktailOverviewDto>
)