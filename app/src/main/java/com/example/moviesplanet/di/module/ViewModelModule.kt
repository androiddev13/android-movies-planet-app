package com.example.moviesplanet.di.module

import androidx.lifecycle.ViewModel
import com.example.moviesplanet.MoviesViewModel
import com.example.moviesplanet.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindMoviesViewModel(moviesViewModel: MoviesViewModel): ViewModel

}