package com.example.moviesplanet.di.module

import androidx.lifecycle.ViewModelProvider
import com.example.moviesplanet.presentation.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}