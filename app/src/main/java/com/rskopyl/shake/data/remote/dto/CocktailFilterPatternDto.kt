package com.rskopyl.shake.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

interface CocktailFilterPatternDto {

    val value: String
}

interface CocktailFilterPatternListDto {

    val patterns: List<CocktailFilterPatternDto>
}

@JsonClass(generateAdapter = true)
data class CategoryFilterPatternDto(
    @Json(name = "strCategory")
    override val value: String
) : CocktailFilterPatternDto

@JsonClass(generateAdapter = true)
data class CategoryFilterPatternListDto(
    @Json(name = "drinks")
    override val patterns: List<CategoryFilterPatternDto>
) : CocktailFilterPatternListDto

@JsonClass(generateAdapter = true)
data class IngredientFilterPatternDto(
    @Json(name = "strIngredient1")
    override val value: String
) : CocktailFilterPatternDto

@JsonClass(generateAdapter = true)
data class IngredientFilterPatternListDto(
    @Json(name = "drinks")
    override val patterns: List<IngredientFilterPatternDto>
) : CocktailFilterPatternListDto

@JsonClass(generateAdapter = true)
data class AlcoholFilterPatternDto(
    @Json(name = "strAlcoholic")
    override val value: String
) : CocktailFilterPatternDto

@JsonClass(generateAdapter = true)
data class AlcoholFilterPatternListDto(
    @Json(name = "drinks")
    override val patterns: List<AlcoholFilterPatternDto>
) : CocktailFilterPatternListDto

@JsonClass(generateAdapter = true)
data class GlassFilterPatternDto(
    @Json(name = "strGlass")
    override val value: String
) : CocktailFilterPatternDto

@JsonClass(generateAdapter = true)
data class GlassFilterPatternListDto(
    @Json(name = "strGlass")
    override val patterns: List<GlassFilterPatternDto>
) : CocktailFilterPatternListDto