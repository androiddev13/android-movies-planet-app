package com.example.data.hilt

import com.example.data.DefaultMoviesRepository
import com.example.data.MoviesRepository
import com.example.data.storage.local.MoviesLocalDataSource
import com.example.data.storage.local.MoviesPreferences
import com.example.data.storage.local.db.MovieDao
import com.example.data.storage.remote.MoviesRemoteDataSource
import com.example.data.storage.remote.MoviesServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideMoviesRepository(moviesRemoteDataSource: MoviesRemoteDataSource,
                                moviesLocalDataSource: MoviesLocalDataSource): MoviesRepository {
        return DefaultMoviesRepository(moviesRemoteDataSource, moviesLocalDataSource)
    }

    @Singleton
    @Provides
    fun provideMoviesRemoteDataSource(api: MoviesServiceApi,
                                      moviesLocalDataSource: MoviesLocalDataSource): MoviesRemoteDataSource {
        return MoviesRemoteDataSource(api, moviesLocalDataSource)
    }

    @Singleton
    @Provides
    fun provideMoviesLocalDataSource(moviesPreferences: MoviesPreferences, movieDao: MovieDao): MoviesLocalDataSource {
        return MoviesLocalDataSource(moviesPreferences, movieDao)
    }
}