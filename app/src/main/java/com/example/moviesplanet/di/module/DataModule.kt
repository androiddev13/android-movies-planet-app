package com.example.moviesplanet.di.module

import com.example.moviesplanet.data.AppPreferences
import com.example.moviesplanet.data.DefaultMoviesRepository
import com.example.moviesplanet.data.MoviesRepository
import com.example.moviesplanet.data.MoviesServiceApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideMoviesRepository(api: MoviesServiceApi, appPreferences: AppPreferences): MoviesRepository {
        return DefaultMoviesRepository(api, appPreferences)
    }

}