package com.rskopyl.shake.data.local.entity

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CocktailVisitEntity(
    @PrimaryKey var cocktailId: String,
    var cocktailName: String,
    var cocktailImage: String,
    var visitDateTime: RealmInstant
) : RealmObject {

    constructor() : this(
        cocktailId = "",
        cocktailName = "",
        cocktailImage = "",
        visitDateTime = RealmInstant.MIN
    )
}