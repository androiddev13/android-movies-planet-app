package com.example.moviesplanet.data.storage.remote

import com.example.moviesplanet.data.model.SortingOption
import com.example.moviesplanet.data.storage.local.MoviesLocalDataSource
import com.example.moviesplanet.data.storage.remote.model.GenreListResponse
import com.example.moviesplanet.data.storage.remote.model.MovieListResponse
import com.example.moviesplanet.data.storage.remote.model.ReviewListResponse
import com.example.moviesplanet.data.storage.remote.model.VideoListResponse
import io.reactivex.Single

class MoviesRemoteDataSource constructor(private val api: MoviesServiceApi,
                                         private val localDataSource: MoviesLocalDataSource) {

    fun getMovies(page: Long): Single<MovieListResponse> {
        return api.getMovies(localDataSource.getSortingOption().toSort(), page)
    }

    fun getMovieVideos(id: String): Single<VideoListResponse> {
        return api.getMovieVideos(id)
    }

    fun getMovieReviews(id: String): Single<ReviewListResponse> {
        return api.getMovieReviews(id)
    }

    fun getGenres(): Single<GenreListResponse> {
        return api.getMovieGenres()
    }

    private fun SortingOption.toSort(): String {
        return when(this) {
            SortingOption.POPULAR -> MoviesServiceApi.KEY_SORT_POPULAR
            SortingOption.TOP_RATED -> MoviesServiceApi.KEY_SORT_TOP_RATED
            SortingOption.UPCOMING -> MoviesServiceApi.KEY_SORT_UPCOMING
        }
    }
}