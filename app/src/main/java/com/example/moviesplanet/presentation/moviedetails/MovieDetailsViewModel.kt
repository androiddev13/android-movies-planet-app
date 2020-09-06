package com.example.moviesplanet.presentation.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.MoviesRepository
import com.example.data.model.LoadingStatus
import com.example.data.model.Movie
import com.example.data.model.MovieDetails
import com.example.data.model.MovieExternalInfo
import com.example.moviesplanet.presentation.ExternalWebPageNavigation
import com.example.moviesplanet.presentation.Navigation
import com.example.moviesplanet.presentation.generic.LiveDataEvent
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
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

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

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
        val disposable = moviesRepository.removeFromFavorite(movie)
            .doOnSubscribe { _favoriteLoadingIndicatorLiveData.value = true }
            .subscribe({
                _favoriteLoadingIndicatorLiveData.value = false
                _movieDetailsLiveData.value = _movieDetailsLiveData.value?.copy(isFavorite = false)
            }, {
                Timber.d(it)
                _favoriteLoadingIndicatorLiveData.value = false
            })
        compositeDisposable.add(disposable)
    }

    private fun addMovieToFavorites() {
        val disposable = moviesRepository.addToFavorite(movie)
            .doOnSubscribe { _favoriteLoadingIndicatorLiveData.value = true }
            .subscribe({
                _favoriteLoadingIndicatorLiveData.value = false
                _movieDetailsLiveData.value = _movieDetailsLiveData.value?.copy(isFavorite = true)
            }, {
                Timber.d(it)
                _favoriteLoadingIndicatorLiveData.value = false
            })
        compositeDisposable.add(disposable)
    }

    private fun loadDetails() {
        val disposable = moviesRepository.getMovieDetails(movie)
            .doOnSubscribe { _loadingStatusLiveData.value = LoadingStatus.LOADING }
            .subscribe({
                onDetailsLoadSuccessful(it)
            }, {
                onDetailsLoadFailed(it)
            })
        compositeDisposable.add(disposable)
    }

    private fun onDetailsLoadSuccessful(movieDetails: MovieDetails) {
        _loadingStatusLiveData.value = LoadingStatus.LOADING_SUCCESS
        _movieDetailsLiveData.value = movieDetails
    }

    private fun onDetailsLoadFailed(throwable: Throwable) {
        Timber.d(throwable)
        _loadingStatusLiveData.value = LoadingStatus.loadingError(throwable.message)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}