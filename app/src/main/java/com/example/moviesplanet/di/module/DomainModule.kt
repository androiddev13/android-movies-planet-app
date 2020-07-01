package com.example.moviesplanet.di.module

import com.example.moviesplanet.data.MoviesRepository
import com.example.moviesplanet.domain.GetSettingsUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideGetSettingsUseCase(moviesRepository: MoviesRepository): GetSettingsUseCase {
        return GetSettingsUseCase(moviesRepository)
    }
}