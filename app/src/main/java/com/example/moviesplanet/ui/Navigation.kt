package com.example.moviesplanet.ui

import com.example.data.model.Movie

sealed class Navigation
object MyFavoritesNavigation : Navigation()
object SettingsNavigation : Navigation()
class MovieDetailsNavigation(val movie: Movie): Navigation()
class ExternalWebPageNavigation(val url: String): Navigation()