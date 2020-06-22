package com.example.moviesplanet.data

import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.data.model.MovieDetails
import com.example.moviesplanet.data.model.SortingOption
import io.reactivex.Completable
import io.reactivex.Single

interface MoviesRepository {

    fun getMovies(page: Long): Single<List<Movie>>

    fun getMovieDetails(movie: Movie): Single<MovieDetails>

    fun setCurrentSortingOption(sortingOption: SortingOption)

    fun addToFavorite(movie: Movie): Completable

    fun removeFromFavorite(movie: Movie): Completable

    fun getFavoriteMovies(): Single<List<Movie>>

}