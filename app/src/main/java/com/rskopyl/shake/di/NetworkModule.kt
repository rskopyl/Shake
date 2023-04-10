package com.rskopyl.shake.di

import com.rskopyl.shake.data.remote.api.CocktailApi
import com.rskopyl.shake.data.remote.api.CocktailFilterPatternApi
import com.rskopyl.shake.data.remote.api.CocktailOverviewApi
import com.rskopyl.shake.util.COCKTAIL_API_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Reusable
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(COCKTAIL_API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCocktailOverviewApi(
        retrofit: Retrofit
    ): CocktailOverviewApi =
        retrofit.create()

    @Provides
    @Singleton
    fun provideCocktailFilterPatternApi(
        retrofit: Retrofit
    ): CocktailFilterPatternApi =
        retrofit.create()

    @Provides
    @Singleton
    fun provideCocktailApi(
        retrofit: Retrofit
    ): CocktailApi =
        retrofit.create()
}