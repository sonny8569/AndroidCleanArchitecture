package com.example.device.module

import android.content.Context
import com.example.device.SharedPreference
import com.example.device.dataSource.SharedDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferenceModule {
    @Provides
    fun provideShared(@ApplicationContext context: Context): SharedDataSource {
        return SharedPreference(context)
    }
}