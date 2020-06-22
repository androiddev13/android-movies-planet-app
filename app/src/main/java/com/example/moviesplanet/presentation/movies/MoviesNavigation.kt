package com.example.moviesplanet.presentation.movies

import com.example.moviesplanet.data.model.Movie

enum class Navigation {
    MOVIE_DETAILS,
    MY_FAVORITES
}

data class MoviesNavigation private constructor(val navigation: Navigation, val movie: Movie = Movie.getEmpty()) {
    companion object {
        fun toMovieDetails(movie: Movie) = MoviesNavigation(Navigation.MOVIE_DETAILS, movie)
        fun toMyFavorites() = MoviesNavigation(Navigation.MY_FAVORITES)
    }
}