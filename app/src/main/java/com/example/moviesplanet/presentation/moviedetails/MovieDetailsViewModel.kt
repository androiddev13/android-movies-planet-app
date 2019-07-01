package com.example.moviesplanet.presentation.moviedetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesplanet.data.MoviesRepository
import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.data.model.MovieExternalInfo
import com.example.moviesplanet.presentation.generic.LiveDataEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {

    val movieDetailsLiveData = MutableLiveData<MovieDetails>()

    val navigateToInfoViewLiveData = MutableLiveData<LiveDataEvent<String>>()

    val loadingIndicatorLiveData = MutableLiveData<Boolean>()

    val loadFailedLiveData = MutableLiveData<Boolean>()

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

    private fun loadDetails() {
        loadingIndicatorLiveData.value = true
        loadFailedLiveData.value = false
        val disposable = moviesRepository.getMovieExternalInfo(movie).subscribe({
            onLoadSuccessful(it)
        }, {
            onLoadFailed(it)
        })
        compositeDisposable.add(disposable)
    }

    private fun onLoadSuccessful(externalInfo: List<MovieExternalInfo>) {
        loadingIndicatorLiveData.value = false
        movieDetailsLiveData.value = MovieDetails(movie, externalInfo)
    }

    private fun onLoadFailed(throwable: Throwable) {
        Log.d(KEY_LOG, throwable.message)
        loadingIndicatorLiveData.value = false
        loadFailedLiveData.value = true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    data class MovieDetails(val movie: Movie, val externalInfo: List<MovieExternalInfo>)

    companion object {
        const val KEY_LOG = "Movie_detail_view_model"
    }
}