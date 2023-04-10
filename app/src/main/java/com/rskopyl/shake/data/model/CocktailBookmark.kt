package com.rskopyl.shake.data.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class CocktailBookmark(
    val cocktailId: String,
    val cocktailName: String,
    val cocktailImage: String,
    val savingDateTime: LocalDateTime
) {
    constructor(
        cocktail: Cocktail,
        savingDateTime: LocalDateTime = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
    ) : this(
        cocktailId = cocktail.id,
        cocktailName = cocktail.name,
        cocktailImage = cocktail.image,
        savingDateTime = savingDateTime
    )
}