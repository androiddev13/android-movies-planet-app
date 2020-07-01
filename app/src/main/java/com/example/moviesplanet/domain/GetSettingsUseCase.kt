package com.example.moviesplanet.domain

import com.example.moviesplanet.data.MoviesRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {
}