package com.rskopyl.shake.data.model

data class CocktailFilter(
    val purpose: Purpose,
    val pattern: String? = null
) {
    enum class Purpose {
        CATEGORY, INGREDIENT, ALCOHOL, GLASS
    }
}