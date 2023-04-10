package com.rskopyl.shake.repository.impl.mapper

import com.rskopyl.shake.data.local.entity.CocktailIngredientEntity
import com.rskopyl.shake.data.model.CocktailIngredient

object CocktailIngredientMapper :
    BiMapper<CocktailIngredientEntity, CocktailIngredient> {

    override fun from(entity: CocktailIngredientEntity): CocktailIngredient {
        return CocktailIngredient(
            name = entity.name,
            measure = entity.measure
        )
    }

    override fun to(entity: CocktailIngredient): CocktailIngredientEntity {
        return CocktailIngredientEntity(
            name = entity.name,
            measure = entity.measure
        )
    }
}