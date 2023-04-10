package com.rskopyl.shake.data.local.entity

import io.realm.kotlin.types.EmbeddedRealmObject

class CocktailIngredientEntity(
    var name: String,
    var measure: String?
) : EmbeddedRealmObject {

    constructor() : this(name = "", measure = null)
}