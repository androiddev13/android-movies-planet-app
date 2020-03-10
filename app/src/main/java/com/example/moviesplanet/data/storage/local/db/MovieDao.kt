package com.example.moviesplanet.data.storage.local.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovie(movie: MovieEntity): Completable

    @Delete
    fun removeMovie(movie: MovieEntity): Completable

    @Query("SELECT * from movieentity")
    fun getMovies(): Single<List<MovieEntity>>

}