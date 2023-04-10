package com.rskopyl.shake.data.local.entity

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CocktailBookmarkEntity(
    @PrimaryKey var cocktailId: String,
    var cocktailName: String,
    var cocktailImage: String,
    var savingDateTime: RealmInstant
) : RealmObject {

    constructor() : this(
        cocktailId = "",
        cocktailName = "",
        cocktailImage = "",
        savingDateTime = RealmInstant.MIN
    )
}