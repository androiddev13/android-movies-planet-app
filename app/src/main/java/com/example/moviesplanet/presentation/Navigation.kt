package com.example.moviesplanet.presentation

import com.example.moviesplanet.data.model.Movie

sealed class Navigation
object MyFavoritesNavigation : Navigation()
object SettingsNavigation : Navigation()
class MovieDetailsNavigation(val movie: Movie): Navigation()
class ExternalWebPageNavigation(val url: String): Navigation()