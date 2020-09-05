package com.example.moviesplanet.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moviesplanet.data.model.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviesDataSourceFactory(private val moviesRepository: MoviesRepository,
                              private val compositeDisposable: CompositeDisposable) : DataSource.Factory<Long, Movie>() {

    private val _repositoryDataSourceLiveData = MutableLiveData<MoviesPageKeyedDataSource>()
    val repositoryDataSourceLiveData: LiveData<MoviesPageKeyedDataSource>
        get() = _repositoryDataSourceLiveData

    override fun create(): DataSource<Long, Movie> {
        val moviesDataSource = MoviesPageKeyedDataSource(moviesRepository, compositeDisposable)
        _repositoryDataSourceLiveData.postValue(moviesDataSource)
        return moviesDataSource
    }
}