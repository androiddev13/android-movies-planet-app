package com.example.moviesplanet.presentation.favorites

import androidx.lifecycle.ViewModel
import com.example.moviesplanet.data.MoviesRepository
import javax.inject.Inject

class MyFavoritesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {
}