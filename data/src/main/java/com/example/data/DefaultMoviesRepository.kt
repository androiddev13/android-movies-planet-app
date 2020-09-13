package com.example.data

import com.example.data.model.*
import com.example.data.storage.local.MoviesLocalDataSource
import com.example.data.storage.remote.MoviesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class DefaultMoviesRepository constructor(private val remoteDataSource: MoviesRemoteDataSource,
                                          private val localDataSource: MoviesLocalDataSource) : MoviesRepository {

    override suspend fun getMovies(page: Long): Result<List<Movie>> {
        return when (val result = remoteDataSource.getMovies(page)) {
            is Result.Success -> {
                val movies = result.data.results?.map { it.toMovie() }?: listOf()
                Result.Success(movies)
            }
            is Result.Error -> result
        }
    }

    override suspend fun getMovieDetails(movie: Movie): Result<MovieDetails> {
        val externalInfo = getMovieExternalInfo(movie)
        val favorites = localDataSource.getFavoritesMovie().first()
        val movieGenres = getMovieGenres(movie)
        if (externalInfo is Result.Error) {
            return externalInfo
        }
        return Result.Success(MovieDetails(movie, isFavoriteMovie(favorites, movie.id), (externalInfo as Result.Success).data, movieGenres))
    }

    private suspend fun getMovieExternalInfo(movie: Movie): Result<List<MovieExternalInfo>> {
        val id = movie.id.toString()
        val reviews = getMovieReviewsExternalInfo(id)
        val videos = getMovieVideosExternalInfo(id)
        if (reviews is Result.Error) {
            return reviews
        }
        if (videos is Result.Error) {
            return videos
        }
        val externalInfo = (reviews as Result.Success).data + (videos as Result.Success).data
        return Result.Success(externalInfo)
    }

    override suspend fun setCurrentSortingOption(sortingOption: SortingOption) {
        localDataSource.setSortingOption(sortingOption)
    }

    override suspend fun addToFavorite(movie: Movie) {
        localDataSource.addMovieToFavorites(movie)
    }

    override suspend fun removeFromFavorite(movie: Movie) {
        localDataSource.removeMovieFromFavorites(movie)
    }

    override suspend fun getFavoriteMovies(): Flow<List<Movie>> {
        return localDataSource.getFavoritesMovie()
    }

    private suspend fun getMovieReviewsExternalInfo(id: String): Result<List<MovieExternalInfo>> {
        return when (val result = remoteDataSource.getMovieReviews(id)) {
            is Result.Success -> {
                val reviews = result.data.results?.map { it.toMovieExternalInfo() }?: listOf()
                Result.Success(reviews)
            }
            is Result.Error -> result
        }
    }

    private suspend fun getMovieVideosExternalInfo(id: String): Result<List<MovieExternalInfo>> {
        return when (val result = remoteDataSource.getMovieVideos(id)) {
            is Result.Success -> {
                val videos = result.data.results?.map { it.toMovieExternalInfo() }?: listOf()
                Result.Success(videos)
            }
            is Result.Error -> result
        }
    }

    private fun isFavoriteMovie(favorites: List<Movie>, id: Int): Boolean {
        return favorites.find { it.id == id } != null
    }

    private suspend fun getMovieGenres(movie: Movie): List<MovieGenre> {
        return when (val result = getGenres()) {
            is Result.Success -> result.data.filter { movie.genres.contains(it.id) }
            is Result.Error -> listOf()
        }
    }

    private suspend fun getGenres(): Result<List<MovieGenre>> {
        return when (val result = remoteDataSource.getMovieGenres()) {
            is Result.Success -> {
                val genres = result.data.genres?.map { MovieGenre(it.id, it.name) }?: listOf()
                return Result.Success(genres)
            }
            is Result.Error -> result
        }
    }
}