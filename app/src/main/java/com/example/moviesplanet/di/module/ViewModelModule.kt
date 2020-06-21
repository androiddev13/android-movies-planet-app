package com.example.moviesplanet.di.module

import androidx.lifecycle.ViewModel
import com.example.moviesplanet.presentation.movies.MoviesViewModel
import com.example.moviesplanet.di.ViewModelKey
import com.example.moviesplanet.presentation.favorites.MyFavoritesViewModel
import com.example.moviesplanet.presentation.moviedetails.MovieDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindMoviesViewModel(moviesViewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindMovieDetailsViewModel(movieDetailsViewModel: MovieDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyFavoritesViewModel::class)
    abstract fun bindMyFavoritesViewModel(myFavoritesViewModel: MyFavoritesViewModel): ViewModel

}