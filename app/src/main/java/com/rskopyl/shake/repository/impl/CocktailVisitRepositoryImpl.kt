package com.rskopyl.shake.repository.impl

import com.rskopyl.shake.data.local.entity.CocktailVisitEntity
import com.rskopyl.shake.data.model.CocktailVisit
import com.rskopyl.shake.di.ApplicationScope
import com.rskopyl.shake.repository.CocktailVisitRepository
import com.rskopyl.shake.repository.impl.mapper.CocktailVisitMapper
import com.rskopyl.shake.util.Resource
import io.realm.kotlin.MutableRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CocktailVisitRepositoryImpl @Inject constructor(
    private val realm: Realm,
    @ApplicationScope
    private val applicationScope: CoroutineScope
) : CocktailVisitRepository {

    private fun MutableRealm.deleteOverflow() {
        val visitCount = query<CocktailVisitEntity>().count().find()
        if (visitCount > HISTORY_SIZE_LIMIT) {
            val overflowVisits = query<CocktailVisitEntity>()
                .sort("visitDateTime")
                .limit(visitCount.toInt() - HISTORY_SIZE_LIMIT)
                .find()
            delete(overflowVisits)
        }
    }

    override fun getAll(): Flow<Resource<List<CocktailVisit>>> = channelFlow {
        send(Resource.Loading())
        realm
            .query<CocktailVisitEntity>()
            .asFlow()
            .flowOn(Dispatchers.IO)
            .collectLatest { change ->
                val visits = CocktailVisitMapper.from(change.list)
                send(Resource.Success(visits))
            }
    }

    override fun insert(visit: CocktailVisit) {
        applicationScope.launch(Dispatchers.IO) {
            realm.write {
                val entity = CocktailVisitMapper.to(visit)
                copyToRealm(entity, updatePolicy = UpdatePolicy.ALL)
                deleteOverflow()
            }
        }
    }

    private companion object {

        const val HISTORY_SIZE_LIMIT = 50
    }
}