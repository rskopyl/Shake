package com.rskopyl.shake.repository.impl.mapper

import com.rskopyl.shake.data.local.entity.CocktailEntity
import com.rskopyl.shake.data.model.Cocktail
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.ext.toRealmSet
import org.mongodb.kbson.ObjectId

object CustomCocktailMapper : BiMapper<CocktailEntity, Cocktail> {

    override fun from(entity: CocktailEntity): Cocktail {
        return Cocktail(
            id = entity.id.toHexString(),
            name = entity.name,
            tags = entity.tags.toSet(),
            ingredients = entity.ingredients
                .map(CocktailIngredientMapper::from),
            instructions = entity.instructions,
            image = entity.image,
            origin = Cocktail.Origin.CUSTOM
        )
    }

    override fun to(entity: Cocktail): CocktailEntity {
        return CocktailEntity(
            id = ObjectId(entity.id),
            name = entity.name,
            tags = entity.tags.toRealmSet(),
            ingredients = entity.ingredients
                .map(CocktailIngredientMapper::to)
                .toRealmList(),
            instructions = entity.instructions,
            image = entity.image
        )
    }
}