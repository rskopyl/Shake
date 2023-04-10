package com.rskopyl.shake.repository.impl.mapper

import com.rskopyl.shake.data.local.entity.CocktailBookmarkEntity
import com.rskopyl.shake.data.model.CocktailBookmark
import com.rskopyl.shake.util.toLocalDateTime
import com.rskopyl.shake.util.toRealmInstant

object CocktailBookmarkMapper :
    BiMapper<CocktailBookmarkEntity, CocktailBookmark> {

    override fun from(entity: CocktailBookmarkEntity): CocktailBookmark {
        return CocktailBookmark(
            cocktailId = entity.cocktailId,
            cocktailName = entity.cocktailName,
            cocktailImage = entity.cocktailImage,
            savingDateTime = entity.savingDateTime.toLocalDateTime()
        )
    }

    override fun to(entity: CocktailBookmark): CocktailBookmarkEntity {
        return CocktailBookmarkEntity(
            cocktailId = entity.cocktailId,
            cocktailName = entity.cocktailName,
            cocktailImage = entity.cocktailImage,
            savingDateTime = entity.savingDateTime.toRealmInstant()
        )
    }
}