package com.example.moviesplanet.data.storage.remote

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesServiceApi {

    @GET("3/movie/{sort}")
    fun getMovies(@Path("sort") sorted: String, @Query("page") page: Int): Single<MovieListResponse>

    @GET("3/movie/{id}/videos")
    fun getMovieVideos(@Path("id") id: String): Single<VideoListResponse>

    @GET("3/movie/{id}/reviews")
    fun getMovieReviews(@Path("id") id: String): Single<ReviewListResponse>

    companion object {
        const val MOVIES_BASE_URL = "http://api.themoviedb.org/"
        const val MOVIES_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/"
        const val MOVIES_VIDEO_BASE_URL = "http://youtube.com/watch?v="
    }
}