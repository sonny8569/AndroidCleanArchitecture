package com.example.network.module

import com.example.data.dataSoruce.NetworkDataSource
import com.example.network.NetworkController
import com.example.network.api.SearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkDataSourceModule {
    @Provides
    fun provideNetWorkDataSource(
        searchApi: SearchApi,
    ): NetworkDataSource {
        return NetworkController(searchApi)
    }
}