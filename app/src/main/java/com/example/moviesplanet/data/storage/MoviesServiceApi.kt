package com.example.moviesplanet.data.storage

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesServiceApi {

    // Get list of movies.
    @GET("3/movie/{sort}")
    fun listMovies(@Path("sort") sorted: String, @Query("page") page: Int): Single<MovieListResponse>
}