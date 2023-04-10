package com.rskopyl.shake.repository.impl.mapper

import com.rskopyl.shake.data.model.Cocktail
import com.rskopyl.shake.data.model.CocktailIngredient
import com.rskopyl.shake.data.remote.dto.CocktailDto
import com.rskopyl.shake.data.remote.dto.CocktailListDto
import com.rskopyl.shake.data.remote.dto.ingredients
import com.rskopyl.shake.data.remote.dto.measures

object PublicCocktailMapper : Mapper<CocktailDto, Cocktail> {

    override fun from(entity: CocktailDto): Cocktail {
        return Cocktail(
            id = entity.id,
            name = entity.name.trim(),
            tags = entity.run {
                setOfNotNull(category, ibaClass, alcohol, glass)
            },
            ingredients = entity.run {
                ingredients.mapIndexed { index, name ->
                    CocktailIngredient(
                        name = name.trim(),
                        measure = measures[index]?.trim()
                    )
                }
            },
            instructions = entity.instructions.trim(),
            image = entity.image,
            origin = Cocktail.Origin.PUBLIC
        )
    }
}

object PublicCocktailListMapper : Mapper<CocktailListDto, List<Cocktail>> {

    override fun from(entity: CocktailListDto): List<Cocktail> =
        entity.cocktails.map(PublicCocktailMapper::from)
}