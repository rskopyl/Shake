package com.rskopyl.shake.data.local.entity

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class CocktailEntity(
    @PrimaryKey var id: ObjectId = ObjectId(),
    var name: String,
    var tags: RealmSet<String>,
    var ingredients: RealmList<CocktailIngredientEntity>,
    var instructions: String,
    var image: String
) : RealmObject {

    constructor() : this(
        name = "",
        tags = realmSetOf(),
        ingredients = realmListOf(),
        instructions = "",
        image = ""
    )
}