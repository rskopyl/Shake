package com.rskopyl.shake.di

import com.rskopyl.shake.repository.*
import com.rskopyl.shake.repository.impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BindingModule {

    @Binds
    @Singleton
    fun bindCocktailRepository(
        impl: CocktailRepositoryImpl
    ): CocktailRepository

    @Binds
    @Singleton
    fun bindCocktailOverviewRepository(
        impl: CocktailOverviewRepositoryImpl
    ): CocktailOverviewRepository

    @Binds
    @Singleton
    fun bindCocktailFilterPatternRepository(
        impl: CocktailFilterPatternRepositoryImpl
    ): CocktailFilterPatternRepository

    @Binds
    @Singleton
    fun bindCocktailBookmarkRepository(
        impl: CocktailBookmarkRepositoryImpl
    ): CocktailBookmarkRepository

    @Binds
    @Singleton
    fun bindCocktailVisitRepository(
        impl: CocktailVisitRepositoryImpl
    ): CocktailVisitRepository
}