package com.example.data.storage.local.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Dao to work with favorite movies and data which is related to them.
 */
@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: MovieEntity)

    @Delete
    suspend fun removeMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGenres(genresEntity: List<GenreEntity>)

    @Insert
    suspend fun addMovieGenres(movieGenres: List<MovieGenreCrossRef>)

    @Query("DELETE from moviegenrecrossref WHERE movieId = :movieId")
    suspend fun removeMovieGenres(movieId: Int)

    @Transaction
    @Query("SELECT * from movieentity")
    fun getMoviesWithGenres(): Flow<List<MovieWithGenres>>
}