package com.rskopyl.shake.di

import com.rskopyl.shake.data.local.entity.CocktailBookmarkEntity
import com.rskopyl.shake.data.local.entity.CocktailEntity
import com.rskopyl.shake.data.local.entity.CocktailIngredientEntity
import com.rskopyl.shake.data.local.entity.CocktailVisitEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRealm(): Realm {
        val configuration = RealmConfiguration
            .Builder(
                schema = setOf(
                    CocktailBookmarkEntity::class,
                    CocktailVisitEntity::class,
                    CocktailIngredientEntity::class,
                    CocktailEntity::class
                )
            )
            .build()
        return Realm.open(configuration)
    }
}