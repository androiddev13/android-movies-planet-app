package com.example.moviesplanet.presentation.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MoviesRepository
import com.example.data.model.LoadingStatus
import com.example.data.model.Movie
import com.example.data.model.MovieDetails
import com.example.data.model.MovieExternalInfo
import com.example.data.model.Result
import com.example.data.utility.exhaustive
import com.example.moviesplanet.presentation.ExternalWebPageNavigation
import com.example.moviesplanet.presentation.Navigation
import com.example.moviesplanet.presentation.generic.LiveDataEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {

    private val _movieDetailsLiveData = MutableLiveData<MovieDetails>()
    val movieDetailsLiveData: LiveData<MovieDetails>
        get() = _movieDetailsLiveData

    private val _navigationLiveData = MutableLiveData<LiveDataEvent<Navigation>>()
    val navigationLiveData: LiveData<LiveDataEvent<Navigation>>
        get() = _navigationLiveData

    private val _loadingStatusLiveData = MutableLiveData<LoadingStatus>()
    val loadingStatusLiveData: LiveData<LoadingStatus>
        get() = _loadingStatusLiveData

    private val _favoriteLoadingIndicatorLiveData = MutableLiveData<Boolean>()
    val favoriteLoadingIndicatorLiveData: LiveData<Boolean>
        get() = _favoriteLoadingIndicatorLiveData

    private var movie: Movie = Movie.getEmpty()

    fun onTryAgainClick() {
        loadDetails()
    }

    fun setMovie(movie: Movie) {
        if (this.movie == movie) {
            return
        }
        this.movie = movie
        loadDetails()
    }

    fun onExternalInfoClick(externalInfo: MovieExternalInfo) {
        _navigationLiveData.value = LiveDataEvent(ExternalWebPageNavigation(externalInfo.url))
    }

    fun toggleFavMovie() {
        if (_movieDetailsLiveData.value?.isFavorite == true) {
            removeMovieFromFavorites()
        } else {
            addMovieToFavorites()
        }
    }

    private fun removeMovieFromFavorites()  {
        _favoriteLoadingIndicatorLiveData.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                moviesRepository.removeFromFavorite(movie)
            }
            _favoriteLoadingIndicatorLiveData.value = false
            _movieDetailsLiveData.value = _movieDetailsLiveData.value?.copy(isFavorite = false)
        }
    }

    private fun addMovieToFavorites() {
        _favoriteLoadingIndicatorLiveData.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                moviesRepository.addToFavorite(movie)
            }
            _favoriteLoadingIndicatorLiveData.value = false
            _movieDetailsLiveData.value = _movieDetailsLiveData.value?.copy(isFavorite = true)
        }
    }

    private fun loadDetails() {
        _loadingStatusLiveData.value = LoadingStatus.LOADING
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                moviesRepository.getMovieDetails(movie)
            }
            when(result) {
                is Result.Success -> onDetailsLoadSuccessful(result.data)
                is Result.Error -> onDetailsLoadFailed(result.exception)
            }.exhaustive
        }
    }

    private fun onDetailsLoadSuccessful(movieDetails: MovieDetails) {
        _loadingStatusLiveData.value = LoadingStatus.LOADING_SUCCESS
        _movieDetailsLiveData.value = movieDetails
    }

    private fun onDetailsLoadFailed(throwable: Throwable) {
        Timber.d(throwable)
        _loadingStatusLiveData.value = LoadingStatus.loadingError(throwable.message)
    }
}