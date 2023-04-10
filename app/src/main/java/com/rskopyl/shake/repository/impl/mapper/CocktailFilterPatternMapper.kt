package com.rskopyl.shake.repository.impl.mapper

import com.rskopyl.shake.data.remote.dto.CocktailFilterPatternDto
import com.rskopyl.shake.data.remote.dto.CocktailFilterPatternListDto

object CocktailFilterPatternMapper : Mapper<CocktailFilterPatternDto, String> {

    override fun from(entity: CocktailFilterPatternDto): String = entity.value
}

object CocktailFilterPatternListMapper :
    Mapper<CocktailFilterPatternListDto, Set<String>> {

    override fun from(entity: CocktailFilterPatternListDto): Set<String> =
        entity.patterns.map(CocktailFilterPatternMapper::from).toSet()
}