package com.example.moviesplanet.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moviesplanet.data.model.Movie
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action

class MoviesDataSource(private val moviesRepository: MoviesRepository,
                       private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Long, Movie>() {

    private val _loadingStatusLiveData = MutableLiveData<PagingLoadingStatus>()
    val loadingStatusLiveData: LiveData<PagingLoadingStatus>
        get() = _loadingStatusLiveData

    private var retryCompletable: Completable? = null

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Movie>) {
        _loadingStatusLiveData.postValue(PagingLoadingStatus.FIRST_LOADING)
        val disposable = moviesRepository.getMovies(1).subscribe({
            setRetry(null)
            _loadingStatusLiveData.postValue(PagingLoadingStatus.FIRST_LOADING_SUCCESS)
            callback.onResult(it, 1, 2)
        }, {
            setRetry(Action { loadInitial(params, callback) })
            _loadingStatusLiveData.postValue(PagingLoadingStatus.firstLoadingError(it.message))
        })
        compositeDisposable.add(disposable)
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {
        _loadingStatusLiveData.postValue(PagingLoadingStatus.LOADING)
        val disposable = moviesRepository.getMovies(params.key).subscribe({
            setRetry(null)
            _loadingStatusLiveData.postValue(PagingLoadingStatus.LOADING_SUCCESS)
            val nextPage = if (it.isNotEmpty())  params.key + 1 else null
            callback.onResult(it, nextPage)
        }, {
            setRetry(Action { loadAfter(params, callback) })
            _loadingStatusLiveData.postValue(PagingLoadingStatus.loadingError(it.message))
        })
        compositeDisposable.add(disposable)
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {
        // TODO stub
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action != null) Completable.fromAction(action) else null
    }
}