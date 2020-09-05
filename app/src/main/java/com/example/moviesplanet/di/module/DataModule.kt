package com.example.moviesplanet.di.module

import com.example.moviesplanet.data.DefaultMoviesRepository
import com.example.moviesplanet.data.MoviesDataSourceFactory
import com.example.moviesplanet.data.MoviesRepository
import com.example.moviesplanet.data.storage.local.MoviesLocalDataSource
import com.example.moviesplanet.data.storage.local.MoviesPreferences
import com.example.moviesplanet.data.storage.local.db.MovieDao
import com.example.moviesplanet.data.storage.remote.MoviesRemoteDataSource
import com.example.moviesplanet.data.storage.remote.MoviesServiceApi
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
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

    @Provides
    fun provideMoviesDataSourceFactory(moviesRepository: MoviesRepository): MoviesDataSourceFactory {
        return MoviesDataSourceFactory(moviesRepository, CompositeDisposable())
    }
}