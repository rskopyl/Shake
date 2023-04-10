package com.rskopyl.shake.repository.impl

import com.rskopyl.shake.data.model.CocktailFilter
import com.rskopyl.shake.data.model.CocktailOverview
import com.rskopyl.shake.data.remote.api.CocktailOverviewApi
import com.rskopyl.shake.repository.CocktailOverviewRepository
import com.rskopyl.shake.repository.impl.mapper.CocktailOverviewListMapper
import com.rskopyl.shake.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CocktailOverviewRepositoryImpl @Inject constructor(
    private val cocktailOverviewApi: CocktailOverviewApi
) : CocktailOverviewRepository {

    override fun getByNameAndFilter(
        name: String,
        filter: CocktailFilter?
    ): Flow<Resource<List<CocktailOverview>>> = flow {
        emit(Resource.Loading())
        try {
            val cocktailOverviews = if (filter?.pattern == null) {
                val dto = withContext(Dispatchers.IO) {
                    cocktailOverviewApi.getByName(name)
                }
                CocktailOverviewListMapper.from(dto)
            } else {
                val dto = withContext(Dispatchers.IO) {
                    cocktailOverviewApi.run {
                        when (filter.purpose) {
                            CocktailFilter.Purpose.CATEGORY ->
                                getByCategory(filter.pattern)
                            CocktailFilter.Purpose.INGREDIENT ->
                                getByIngredient(filter.pattern)
                            CocktailFilter.Purpose.ALCOHOL ->
                                getByAlcohol(filter.pattern)
                            CocktailFilter.Purpose.GLASS ->
                                getByGlass(filter.pattern)
                        }
                    }
                }
                CocktailOverviewListMapper.from(dto).filter { cocktailOverview ->
                    cocktailOverview.name.contains(name, ignoreCase = true)
                }
            }
            emit(Resource.Success(cocktailOverviews))
        } catch (_: Exception) {
            emit(Resource.Error())
        }
    }
}