package com.example.nathanhomework.di

import android.content.Context
import com.example.domain.model.Router
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ActivityComponent :: class)
@Module
abstract class ActivityModule {
    companion object{
        @Provides
        fun provideKaKaoBankRouter(@ApplicationContext context : Context) : Router {
            return KaKaoBankRouter(context)
        }
    }
}