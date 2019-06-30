package com.example.moviesplanet.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesServiceApi {

    // Get list of movies.
    @GET("3/movie/{sort}")
    fun listMovies(@Path("sort") sorted: String): Single<MovieListResponse>
}