package com.rskopyl.shake.repository.impl.mapper

import com.rskopyl.shake.data.model.CocktailOverview
import com.rskopyl.shake.data.remote.dto.CocktailOverviewDto
import com.rskopyl.shake.data.remote.dto.CocktailOverviewListDto

object CocktailOverviewMapper :
    Mapper<CocktailOverviewDto, CocktailOverview> {

    override fun from(entity: CocktailOverviewDto): CocktailOverview {
        return CocktailOverview(
            id = entity.id,
            name = entity.name,
            image = entity.image
        )
    }
}

object CocktailOverviewListMapper :
    Mapper<CocktailOverviewListDto, List<CocktailOverview>> {

    override fun from(entity: CocktailOverviewListDto): List<CocktailOverview> =
        entity.overviews.map(CocktailOverviewMapper::from)
}