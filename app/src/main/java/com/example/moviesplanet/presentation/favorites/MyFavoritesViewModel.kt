package com.example.moviesplanet.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MoviesRepository
import com.example.data.model.Movie
import com.example.data.model.Result
import com.example.moviesplanet.presentation.MovieDetailsNavigation
import com.example.moviesplanet.presentation.Navigation
import com.example.moviesplanet.presentation.generic.LiveDataEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MyFavoritesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {

    private val _favoriteMoviesLiveData = MutableLiveData<List<Movie>>()
    val favoriteMoviesLiveData: LiveData<List<Movie>>
        get() = _favoriteMoviesLiveData

    private val _favoritesNavigationLiveData = MutableLiveData<LiveDataEvent<Navigation>>()
    val favoritesNavigationLiveData: LiveData<LiveDataEvent<Navigation>>
        get() = _favoritesNavigationLiveData

    init {
        loadMovies()
    }

    fun onMovieClick(movie: Movie) {
        _favoritesNavigationLiveData.value = LiveDataEvent(MovieDetailsNavigation(movie))
    }

    private fun loadMovies() {
        viewModelScope.launch {
            moviesRepository.getFavoriteMovies().collect {
                _favoriteMoviesLiveData.value = it
            }
        }
    }
}