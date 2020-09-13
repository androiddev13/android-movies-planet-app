package com.example.data.storage.remote

import com.example.data.model.MovieGenre
import com.example.data.model.Result
import com.example.data.model.SortingOption
import com.example.data.storage.local.MoviesLocalDataSource
import com.example.data.storage.remote.model.*
import com.example.data.utility.safeApiCall
import com.example.data.utility.safeApiResponseHandling

class MoviesRemoteDataSource constructor(private val api: MoviesServiceApi,
                                         private val localDataSource: MoviesLocalDataSource) {

    /**
     * In-memory cache for list of [MovieGenre] to avoid fetching it each time movie details is
     * required during application lifecycle.
     */
    private var genres = listOf<GenreResponse>()

    suspend fun getMovies(page: Long): Result<MovieListResponse> = safeApiCall(
        call = { requestMovies(localDataSource.getSortingOption().toSort(), page) },
        errorMessage = "Error getting movies."
    )

    suspend fun getMovieVideos(id: String): Result<VideoListResponse> = safeApiCall(
        call = { requestMovieVideos(id) },
        errorMessage = "Error getting movie videos"
    )

    suspend fun getMovieReviews(id: String): Result<ReviewListResponse> = safeApiCall(
        call = { requestMovieReviews(id) },
        errorMessage = "Error getting movie reviews"
    )

    suspend fun getMovieGenres(): Result<GenreListResponse> {
        if (genres.isNotEmpty()) {
            return Result.Success(GenreListResponse(genres))
        }
        return safeApiCall(
            call = { requestMovieGenres() },
            errorMessage = "Error getting genres"
        )
    }

    private suspend fun requestMovieGenres(): Result<GenreListResponse> {
        val result = api.getMovieGenres().safeApiResponseHandling("Error getting movie genres")
        if (result is Result.Success) {
            // Cache genres in memory.
            genres = result.data.genres?: listOf()
            // Cache genres in local database.
            localDataSource.addGenres(genres.map { MovieGenre(it.id, it.name) })
        }
        return result
    }

    private suspend fun requestMovieReviews(movieId: String): Result<ReviewListResponse> {
        return api.getMovieReviews(movieId).safeApiResponseHandling("Error getting movie reviews")
    }

    private suspend fun requestMovieVideos(movieId: String): Result<VideoListResponse> {
        return api.getMovieVideos(movieId).safeApiResponseHandling("Error getting movie videos")
    }

    private suspend fun requestMovies(sorting: String, page: Long): Result<MovieListResponse> {
        return api.getMovies(sorting, page).safeApiResponseHandling("Error getting movies")
    }

    private fun SortingOption.toSort(): String {
        return when(this) {
            SortingOption.POPULAR -> MoviesServiceApi.KEY_SORT_POPULAR
            SortingOption.TOP_RATED -> MoviesServiceApi.KEY_SORT_TOP_RATED
            SortingOption.UPCOMING -> MoviesServiceApi.KEY_SORT_UPCOMING
        }
    }
}