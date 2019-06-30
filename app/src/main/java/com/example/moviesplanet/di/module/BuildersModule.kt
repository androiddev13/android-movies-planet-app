package com.example.moviesplanet.di.module

import com.example.moviesplanet.MoviesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMoviesActivity(): MoviesActivity

}