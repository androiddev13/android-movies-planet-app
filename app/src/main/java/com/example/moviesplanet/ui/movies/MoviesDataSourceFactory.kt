package com.example.moviesplanet.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.data.CoroutinesDispatcherProvider
import com.example.data.MoviesRepository
import com.example.data.model.Movie
import kotlinx.coroutines.CoroutineScope

class MoviesDataSourceFactory(private val moviesRepository: MoviesRepository,
                              private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider,
                              private val coroutineScope: CoroutineScope) : DataSource.Factory<Long, Movie>() {

    private val _repositoryDataSourceLiveData = MutableLiveData<MoviesPageKeyedDataSource>()
    val repositoryDataSourceLiveData: LiveData<MoviesPageKeyedDataSource>
        get() = _repositoryDataSourceLiveData

    override fun create(): DataSource<Long, Movie> {
        val moviesDataSource = MoviesPageKeyedDataSource(moviesRepository, coroutinesDispatcherProvider, coroutineScope)
        _repositoryDataSourceLiveData.postValue(moviesDataSource)
        return moviesDataSource
    }
}