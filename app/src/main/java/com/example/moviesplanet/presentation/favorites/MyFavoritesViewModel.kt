package com.example.moviesplanet.presentation.favorites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesplanet.data.MoviesRepository
import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.presentation.generic.LiveDataEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MyFavoritesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {

    private val _favoriteMoviesLiveData = MutableLiveData<List<Movie>>()
    val favoriteMoviesLiveData: LiveData<List<Movie>>
        get() = _favoriteMoviesLiveData

    private val _favoritesNavigationLiveData = MutableLiveData<LiveDataEvent<MyFavoritesNavigation>>()
    val favoritesNavigationLiveData: LiveData<LiveDataEvent<MyFavoritesNavigation>>
        get() = _favoritesNavigationLiveData

    private val compositeDisposable = CompositeDisposable()

    init {
        loadMovies()
    }

    // TODO
    //fun reloadMyFavorites() {
    //    loadMovies()
    //}

    fun onMovieClick(movie: Movie) {
        _favoritesNavigationLiveData.value = LiveDataEvent(MyFavoritesNavigation.toMovieDetails(movie))
    }

    private fun loadMovies() {
        val disposable = moviesRepository.getFavoriteMovies().subscribe({
            _favoriteMoviesLiveData.value = it
        }, {
            Log.d(KEY_LOG, "$it")
            _favoriteMoviesLiveData.value = listOf()
        })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    companion object {
        const val KEY_LOG = "Movie_my_favorites"
    }
}