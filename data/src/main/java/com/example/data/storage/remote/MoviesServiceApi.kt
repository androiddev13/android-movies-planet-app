package com.example.data.storage.remote

import com.example.data.storage.remote.model.GenreListResponse
import com.example.data.storage.remote.model.MovieListResponse
import com.example.data.storage.remote.model.ReviewListResponse
import com.example.data.storage.remote.model.VideoListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesServiceApi {

    @GET("3/movie/{sort}")
    suspend fun getMovies(@Path("sort") sorted: String, @Query("page") page: Long): Response<MovieListResponse>

    @GET("3/movie/{id}/videos")
    suspend fun getMovieVideos(@Path("id") id: String): Response<VideoListResponse>

    @GET("3/movie/{id}/reviews")
    suspend fun getMovieReviews(@Path("id") id: String): Response<ReviewListResponse>

    @GET("3/genre/movie/list")
    suspend fun getMovieGenres(): Response<GenreListResponse>

    companion object {
        const val MOVIES_BASE_URL = "http://api.themoviedb.org/"
        const val MOVIES_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/"
        const val MOVIES_VIDEO_BASE_URL = "http://youtube.com/watch?v="

        const val KEY_SORT_POPULAR = "popular"
        const val KEY_SORT_TOP_RATED = "top_rated"
        const val KEY_SORT_UPCOMING = "upcoming"
    }
}