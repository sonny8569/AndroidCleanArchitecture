package com.example.network.module

import com.example.network.RetrofitFactory
import com.example.network.api.SearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class NetworkModule {
    companion object{
        @Singleton
        @Provides
        fun provideRetrofit() : Retrofit = RetrofitFactory.create()

        @Singleton
        @Provides
        fun provideSearchApi(retrofit: Retrofit) = retrofit.create(SearchApi::class.java)
    }
}