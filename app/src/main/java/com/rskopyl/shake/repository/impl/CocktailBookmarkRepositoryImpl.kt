package com.rskopyl.shake.repository.impl

import com.rskopyl.shake.data.local.entity.CocktailBookmarkEntity
import com.rskopyl.shake.data.model.CocktailBookmark
import com.rskopyl.shake.di.ApplicationScope
import com.rskopyl.shake.repository.CocktailBookmarkRepository
import com.rskopyl.shake.repository.impl.mapper.CocktailBookmarkMapper
import com.rskopyl.shake.util.Resource
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CocktailBookmarkRepositoryImpl @Inject constructor(
    private val realm: Realm,
    @ApplicationScope
    private val applicationScope: CoroutineScope
) : CocktailBookmarkRepository {

    override fun getAll(): Flow<Resource<List<CocktailBookmark>>> = channelFlow {
        send(Resource.Loading())
        realm
            .query<CocktailBookmarkEntity>()
            .asFlow()
            .flowOn(Dispatchers.IO)
            .collectLatest { change ->
                val bookmarks = CocktailBookmarkMapper.from(change.list)
                send(Resource.Success(bookmarks))
            }
    }

    override fun getById(
        id: String
    ): Flow<Resource<CocktailBookmark?>> = channelFlow {
        send(Resource.Loading())
        realm
            .query<CocktailBookmarkEntity>("cocktailId == $0", id)
            .first().asFlow()
            .flowOn(Dispatchers.IO)
            .collectLatest { change ->
                val bookmark = change.obj?.let(CocktailBookmarkMapper::from)
                send(Resource.Success(bookmark))
            }
    }

    override fun insert(bookmark: CocktailBookmark) {
        applicationScope.launch(Dispatchers.IO) {
            realm.write {
                val entity = CocktailBookmarkMapper.to(bookmark)
                copyToRealm(entity)
            }
        }
    }

    override fun deleteById(id: String) {
        applicationScope.launch(Dispatchers.IO) {
            realm.write {
                query<CocktailBookmarkEntity>("cocktailId == $0", id)
                    .first().find()
                    ?.let(::delete)
            }
        }
    }
}