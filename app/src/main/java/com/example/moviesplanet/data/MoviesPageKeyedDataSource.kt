package com.example.moviesplanet.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.data.model.LoadingStatus
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class MoviesPageKeyedDataSource(private val moviesRepository: MoviesRepository,
                                private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Long, Movie>() {

    private val _loadingStatusLiveData = MutableLiveData<LoadingStatus>()
    val loadingStatusLiveData: LiveData<LoadingStatus>
        get() = _loadingStatusLiveData

    private var retryCompletable: Completable? = null

    fun retry() {
        retryCompletable?.let {
            val disposable = it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            compositeDisposable.add(disposable)
        }
    }

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Movie>) {
        val disposable = moviesRepository.getMovies(1)
            .doOnSubscribe { _loadingStatusLiveData.postValue(LoadingStatus.FIRST_LOADING) }
            .subscribe({
                setRetry(null)
                _loadingStatusLiveData.postValue(LoadingStatus.FIRST_LOADING_SUCCESS)
                callback.onResult(it, 1, 2)
            }, {
                setRetry(Action { loadInitial(params, callback) })
                _loadingStatusLiveData.postValue(LoadingStatus.firstLoadingError(it.message))
            })
        compositeDisposable.add(disposable)
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {
        _loadingStatusLiveData.postValue(LoadingStatus.LOADING)
        val disposable = moviesRepository.getMovies(params.key)
            .doOnSubscribe { _loadingStatusLiveData.postValue(LoadingStatus.LOADING) }
            .subscribe({
                setRetry(null)
                _loadingStatusLiveData.postValue(LoadingStatus.LOADING_SUCCESS)
                val nextPage = if (it.isNotEmpty())  params.key + 1 else null
                callback.onResult(it, nextPage)
            }, {
                setRetry(Action { loadAfter(params, callback) })
                _loadingStatusLiveData.postValue(LoadingStatus.loadingError(it.message))
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