package com.example.moviesplanet.presentation.favorites

import com.example.moviesplanet.data.model.Movie

enum class Navigation {
    MOVIE_DETAILS
}

data class MyFavoritesNavigation(val navigation: Navigation, val movie: Movie = Movie.getEmpty()) {
    companion object {
        fun toMovieDetails(movie: Movie) = MyFavoritesNavigation(Navigation.MOVIE_DETAILS, movie)
    }
}