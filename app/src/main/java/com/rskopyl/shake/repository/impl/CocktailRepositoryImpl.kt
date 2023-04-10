package com.rskopyl.shake.repository.impl

import android.content.Context
import androidx.core.net.toUri
import com.rskopyl.shake.data.local.entity.CocktailEntity
import com.rskopyl.shake.data.model.Cocktail
import com.rskopyl.shake.data.remote.api.CocktailApi
import com.rskopyl.shake.di.ApplicationScope
import com.rskopyl.shake.repository.CocktailRepository
import com.rskopyl.shake.repository.impl.mapper.CustomCocktailMapper
import com.rskopyl.shake.repository.impl.mapper.PublicCocktailListMapper
import com.rskopyl.shake.util.*
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class CocktailRepositoryImpl @Inject constructor(
    private val cocktailApi: CocktailApi,
    private val realm: Realm,
    @ApplicationContext private val context: Context,
    @ApplicationScope
    private val applicationScope: CoroutineScope
) : CocktailRepository {

    private fun getCustomById(id: String): Flow<Cocktail?> {
        return realm
            .query<CocktailEntity>("id == $0", ObjectId(id))
            .asFlow()
            .flowOn(Dispatchers.IO)
            .map { change ->
                CustomCocktailMapper.from(change.list).singleOrNull()
            }
    }

    override fun getById(
        id: String
    ): Flow<Resource<Cocktail>> = channelFlow {
        send(Resource.Loading())
        try {
            coroutineScope {
                withContext(Dispatchers.IO) {
                    if (id.toObjectIdOrNull() != null) {
                        getCustomById(id).collectLatest { cocktail ->
                            if (cocktail != null) {
                                send(Resource.Success(cocktail))
                            } else {
                                send(Resource.Error())
                            }
                        }
                    } else {
                        val dto = cocktailApi.getById(id)
                        val cocktail = PublicCocktailListMapper.from(dto)
                        send(Resource.Success(cocktail.single()))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            send(Resource.Error())
        }
    }

    override fun getCustom(): Flow<Resource<List<Cocktail>>> = channelFlow {
        send(Resource.Loading())
        realm
            .query<CocktailEntity>()
            .asFlow()
            .flowOn(Dispatchers.IO)
            .collectLatest { change ->
                val cocktails = CustomCocktailMapper.from(change.list)
                send(Resource.Success(cocktails))
            }
    }

    override fun upsert(cocktail: Cocktail) {
        applicationScope.launch(Dispatchers.IO) {
            val image = context.resolveImage(cocktail.image.toUri())
            val url = context.storeImage(image, GALLERY_FOLDER_NAME)
            if (url != null) {
                realm.write {
                    val entity = CustomCocktailMapper.to(
                        cocktail.copy(image = url.toString())
                    )
                    copyToRealm(entity, updatePolicy = UpdatePolicy.ALL)
                }
            }
        }
    }

    override fun deleteById(id: String) {
        applicationScope.launch(Dispatchers.IO) {
            realm.write {
                query<CocktailEntity>("id == $0", ObjectId(id))
                    .first().find()
                    ?.let(::delete)
            }
        }
    }
}