package com.example.moviesplanet.data.storage.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class, GenreEntity::class, MovieGenreCrossRef::class], version = 1)
abstract class MovieRoomDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {

        private const val MOVIE_DATABASE_NAME = "movie_database"

        fun create(context: Context): MovieRoomDatabase {
            return Room.databaseBuilder(
                context,
                MovieRoomDatabase::class.java,
                MOVIE_DATABASE_NAME
            ).build()
        }
    }

}