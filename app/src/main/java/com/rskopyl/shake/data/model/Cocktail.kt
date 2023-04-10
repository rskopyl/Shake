package com.rskopyl.shake.data.model

import org.mongodb.kbson.ObjectId

data class Cocktail(
    val id: String = ObjectId().toHexString(),
    val name: String,
    val tags: Set<String>,
    val ingredients: List<CocktailIngredient>,
    val instructions: String,
    val image: String,
    val origin: Origin = Origin.CUSTOM
) {
    enum class Origin { PUBLIC, CUSTOM }
}