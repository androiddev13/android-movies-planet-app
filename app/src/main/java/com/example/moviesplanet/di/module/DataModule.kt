package com.example.moviesplanet.di.module

import com.example.moviesplanet.data.storage.local.AppPreferences
import com.example.moviesplanet.data.DefaultMoviesRepository
import com.example.moviesplanet.data.MoviesRepository
import com.example.moviesplanet.data.storage.local.db.MovieDao
import com.example.moviesplanet.data.storage.local.db.MovieRoomDatabase
import com.example.moviesplanet.data.storage.remote.MoviesServiceApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideMoviesRepository(api: MoviesServiceApi,
                                appPreferences: AppPreferences,
                                movieDao: MovieDao): MoviesRepository {
        return DefaultMoviesRepository(api, appPreferences, movieDao)
    }

}