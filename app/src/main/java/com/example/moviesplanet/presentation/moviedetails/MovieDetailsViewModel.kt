package com.example.moviesplanet.presentation.moviedetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesplanet.data.MoviesRepository
import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.data.model.MovieDetails
import com.example.moviesplanet.data.model.MovieExternalInfo
import com.example.moviesplanet.presentation.generic.LiveDataEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {

    val movieDetailsLiveData = MutableLiveData<MovieDetails>()

    val navigateToInfoViewLiveData = MutableLiveData<LiveDataEvent<String>>()

    val loadingIndicatorLiveData = MutableLiveData<Boolean>()

    val loadFailedLiveData = MutableLiveData<Boolean>()

    val favoriteLoadingIndicatorLiveData = MutableLiveData<Boolean>()

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
        navigateToInfoViewLiveData.value = LiveDataEvent(externalInfo.url)
    }

    fun toggleFavMovie() {
        if (movieDetailsLiveData.value?.isFavorite == true) {
            removeMovieFromFavorites()
        } else {
            addMovieToFavorites()
        }
    }

    private fun removeMovieFromFavorites()  {
        val disposable = moviesRepository.removeFromFavorite(movie)
            .doOnSubscribe { favoriteLoadingIndicatorLiveData.value = true }
            .subscribe({
                favoriteLoadingIndicatorLiveData.value = false
                movieDetailsLiveData.value = movieDetailsLiveData.value?.copy(isFavorite = false)
            }, {
                Log.d(KEY_LOG, it.message)
                favoriteLoadingIndicatorLiveData.value = false
            })
        compositeDisposable.add(disposable)
    }

    private fun addMovieToFavorites() {
        favoriteLoadingIndicatorLiveData.value = true
        val disposable = moviesRepository.addToFavorite(movie)
            .doOnSubscribe { favoriteLoadingIndicatorLiveData.value = true }
            .subscribe({
                favoriteLoadingIndicatorLiveData.value = false
                movieDetailsLiveData.value = movieDetailsLiveData.value?.copy(isFavorite = true)
            }, {
                Log.d(KEY_LOG, it.message)
                favoriteLoadingIndicatorLiveData.value = false
            })
        compositeDisposable.add(disposable)
    }

    private fun loadDetails() {
        loadingIndicatorLiveData.value = true
        loadFailedLiveData.value = false
        val disposable = moviesRepository.getMovieDetails(movie).subscribe({
            onDetailsLoadSuccessful(it)
        }, {
            onDetailsLoadFailed(it)
        })
        compositeDisposable.add(disposable)
    }

    private fun onDetailsLoadSuccessful(movieDetails: MovieDetails) {
        loadingIndicatorLiveData.value = false
        movieDetailsLiveData.value = movieDetails
    }

    private fun onDetailsLoadFailed(throwable: Throwable) {
        Log.d(KEY_LOG, throwable.message)
        loadingIndicatorLiveData.value = false
        loadFailedLiveData.value = true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    companion object {
        const val KEY_LOG = "Movie_detail_view_model"
    }
}