package com.example.moviesplanet.data.storage.local.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Dao to work with favorite movies and data which is related to them.
 */
@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovie(movie: MovieEntity): Completable

    @Delete
    fun removeMovie(movie: MovieEntity): Completable

    @Insert
    fun addGenres(genresEntity: List<GenreEntity>): Completable

    @Insert
    fun addMovieGenres(movieGenres: List<MovieGenreCrossRef>): Completable

    @Query("DELETE from moviegenrecrossref WHERE movieId = :movieId")
    fun removeMovieGenre(movieId: Int): Completable

    @Transaction
    @Query("SELECT * from movieentity")
    fun getMoviesWithGenres(): Observable<List<MovieWithGenres>>

    @Query("SELECT * from moviegenrecrossref")
    fun getMovieGenreCrossRef(): Single<List<MovieGenreCrossRef>>
}