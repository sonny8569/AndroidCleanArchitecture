package com.example.device.module

import com.example.data.dataSoruce.DeviceDataSource
import com.example.device.DeviceController
import com.example.device.dataSource.SharedDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DeviceDataSourceModule {

    @Provides
    fun provideSharedDataSource(sharedDataSource: SharedDataSource): DeviceDataSource {
        return DeviceController(sharedDataSource)
    }
}