package com.example.moviesplanet

import androidx.lifecycle.ViewModel
import com.example.moviesplanet.data.MoviesRepository
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {


}