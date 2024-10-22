package com.example.nathanhomework.di

import com.example.data.dataSoruce.NetworkDataSource
import com.example.data.repository.SearchRepository
import com.example.domain.repository.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchRepositoryModule {

    @Provides
    fun provideSearchRepository(networkDataSource: NetworkDataSource): SearchRepository {
        return SearchRepositoryImpl(networkDataSource)
    }
}