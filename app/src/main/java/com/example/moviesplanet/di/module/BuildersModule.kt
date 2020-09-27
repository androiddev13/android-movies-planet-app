package com.example.moviesplanet.di.module

import com.example.moviesplanet.presentation.favorites.MyFavoritesFragment
import com.example.moviesplanet.presentation.moviedetails.MovieDetailsFragment
import com.example.moviesplanet.presentation.movies.MoviesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMoviesFragment(): MoviesFragment

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailsFragment(): MovieDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeMyFavoritesFragment(): MyFavoritesFragment

}