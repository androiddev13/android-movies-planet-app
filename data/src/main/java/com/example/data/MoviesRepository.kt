package com.example.data

import com.example.data.model.Movie
import com.example.data.model.MovieDetails
import com.example.data.model.Result
import com.example.data.model.SortingOption
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getMovies(page: Long): Result<List<Movie>>

    suspend fun getMovieDetails(movie: Movie): Result<MovieDetails>

    suspend fun setCurrentSortingOption(sortingOption: SortingOption)

    suspend fun addToFavorite(movie: Movie)

    suspend fun removeFromFavorite(movie: Movie)

    suspend fun getFavoriteMovies(): Flow<List<Movie>>

}