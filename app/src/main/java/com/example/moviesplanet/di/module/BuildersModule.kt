package com.example.moviesplanet.di.module

import com.example.moviesplanet.presentation.favorites.MyFavoritesActivity
import com.example.moviesplanet.presentation.moviedetails.MovieDetailsActivity
import com.example.moviesplanet.presentation.movies.MoviesActivity
import com.example.moviesplanet.presentation.settings.SettingsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMoviesActivity(): MoviesActivity

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailsActivity(): MovieDetailsActivity

    @ContributesAndroidInjector
    abstract fun contributeMyFavoritesActivity(): MyFavoritesActivity

    @ContributesAndroidInjector
    abstract fun contributeSettingsActivity(): SettingsActivity

}