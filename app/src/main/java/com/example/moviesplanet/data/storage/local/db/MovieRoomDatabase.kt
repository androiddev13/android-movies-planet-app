package com.example.moviesplanet.data.storage.local.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class, GenreEntity::class, MovieGenreCrossRef::class], version = 1)
abstract class MovieRoomDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {

        private const val MOVIE_DATABASE_NAME = "movie_database"

        fun create(application: Application): MovieRoomDatabase {
            return Room.databaseBuilder(
                application.applicationContext,
                MovieRoomDatabase::class.java,
                MOVIE_DATABASE_NAME
            ).build()
        }
    }

}