package com.rskopyl.shake.repository.impl.mapper

import com.rskopyl.shake.data.local.entity.CocktailVisitEntity
import com.rskopyl.shake.data.model.CocktailVisit
import com.rskopyl.shake.util.toLocalDateTime
import com.rskopyl.shake.util.toRealmInstant

object CocktailVisitMapper :
    BiMapper<CocktailVisitEntity, CocktailVisit> {

    override fun from(entity: CocktailVisitEntity): CocktailVisit {
        return CocktailVisit(
            cocktailId = entity.cocktailId,
            cocktailName = entity.cocktailName,
            cocktailImage = entity.cocktailImage,
            visitDateTime = entity.visitDateTime.toLocalDateTime()
        )
    }

    override fun to(entity: CocktailVisit): CocktailVisitEntity {
        return CocktailVisitEntity(
            cocktailId = entity.cocktailId,
            cocktailName = entity.cocktailName,
            cocktailImage = entity.cocktailImage,
            visitDateTime = entity.visitDateTime.toRealmInstant()
        )
    }
}