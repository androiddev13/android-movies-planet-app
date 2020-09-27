package com.example.data.hilt

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.data.storage.local.MoviesPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class PreferencesModule {

    @Provides
    @Singleton
    fun provideMoviesPreferences(sharedPreferences: SharedPreferences): MoviesPreferences {
        return MoviesPreferences(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application)
}
