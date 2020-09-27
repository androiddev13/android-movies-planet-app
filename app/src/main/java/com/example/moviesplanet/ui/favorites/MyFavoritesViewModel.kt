package com.example.moviesplanet.ui.favorites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MoviesRepository
import com.example.data.model.Movie
import com.example.moviesplanet.ui.MovieDetailsNavigation
import com.example.moviesplanet.ui.Navigation
import com.example.moviesplanet.ui.generic.LiveDataEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MyFavoritesViewModel @ViewModelInject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {

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