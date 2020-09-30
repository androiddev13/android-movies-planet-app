package com.example.moviesplanet.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.data.CoroutinesDispatcherProvider
import com.example.data.MoviesRepository
import com.example.data.model.Movie
import com.example.data.model.LoadingStatus
import com.example.data.model.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesPageKeyedDataSource(private val moviesRepository: MoviesRepository,
                                private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider,
                                private val coroutineScope: CoroutineScope) : PageKeyedDataSource<Long, Movie>() {

    private val _loadingStatusLiveData = MutableLiveData<LoadingStatus>()
    val loadingStatusLiveData: LiveData<LoadingStatus>
        get() = _loadingStatusLiveData

    private var retry: (() -> Unit)? = null

    fun retry() {
        retry?.invoke()
    }

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Movie>) {
        _loadingStatusLiveData.postValue(LoadingStatus.FIRST_LOADING)
        coroutineScope.launch(coroutinesDispatcherProvider.io) {
            val result = moviesRepository.getMovies(1)
            withContext(coroutinesDispatcherProvider.main) {
                when (result) {
                    is Result.Success -> {
                        retry = null
                        _loadingStatusLiveData.postValue(LoadingStatus.FIRST_LOADING_SUCCESS)
                        callback.onResult(result.data, 1, 2)
                    }
                    is Result.Error -> {
                        retry = { loadInitial(params, callback) }
                        _loadingStatusLiveData.postValue(LoadingStatus.firstLoadingError(result.exception.message))
                    }
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {
        _loadingStatusLiveData.postValue(LoadingStatus.LOADING)
        coroutineScope.launch(coroutinesDispatcherProvider.io) {
            val result = moviesRepository.getMovies(params.key)
            withContext(coroutinesDispatcherProvider.main) {
                when (result) {
                    is Result.Success -> {
                        retry = null
                        _loadingStatusLiveData.postValue(LoadingStatus.LOADING_SUCCESS)
                        val nextPage = if (result.data.isNotEmpty())  params.key + 1 else null
                        callback.onResult(result.data, nextPage)
                    }
                    is Result.Error -> {
                        retry = { loadAfter(params, callback) }
                        _loadingStatusLiveData.postValue(LoadingStatus.loadingError(result.exception.message))
                    }
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {
        // TODO stub
    }
}