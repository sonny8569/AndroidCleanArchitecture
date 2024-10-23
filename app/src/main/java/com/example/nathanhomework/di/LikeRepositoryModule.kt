package com.example.nathanhomework.di

import com.example.data.dataSoruce.DeviceDataSource
import com.example.data.repository.LikeRepository
import com.example.domain.repository.LikeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class LikeRepositoryModule {

    @Provides
    fun provideLikeRepository(deviceDataSource: DeviceDataSource): LikeRepository {
        return LikeRepositoryImpl(deviceDataSource)
    }
}