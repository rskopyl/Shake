package com.rskopyl.shake.data.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class CocktailVisit(
    val cocktailId: String,
    val cocktailName: String,
    val cocktailImage: String,
    val visitDateTime: LocalDateTime
) {
    constructor(
        cocktail: Cocktail,
        visitDateTime: LocalDateTime = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
    ) : this(
        cocktailId = cocktail.id,
        cocktailName = cocktail.name,
        cocktailImage = cocktail.image,
        visitDateTime = visitDateTime
    )
}