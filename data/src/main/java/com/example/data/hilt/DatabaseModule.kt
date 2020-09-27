package com.example.data.hilt

import android.app.Application
import com.example.data.storage.local.db.MovieDao
import com.example.data.storage.local.db.MovieRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideMovieDatabase(application: Application): MovieRoomDatabase {
        return MovieRoomDatabase.create(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideMovieDao(movieRoomDatabase: MovieRoomDatabase): MovieDao {
        return movieRoomDatabase.movieDao()
    }

}