package com.example.moviesplanet.presentation.movies

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.data.MoviesRepository
import com.example.data.model.LoadingStatus
import com.example.data.model.Movie
import com.example.data.model.SortingOption
import com.example.moviesplanet.presentation.MovieDetailsNavigation
import com.example.moviesplanet.presentation.MyFavoritesNavigation
import com.example.moviesplanet.presentation.Navigation
import com.example.moviesplanet.presentation.SettingsNavigation
import com.example.moviesplanet.presentation.generic.LiveDataEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {

    private val movieDataSourceFactory: MoviesDataSourceFactory = MoviesDataSourceFactory(moviesRepository, viewModelScope)

    private val _moviesLiveData: LiveData<PagedList<Movie>>
    val moviesLiveData: LiveData<PagedList<Movie>>
        get() = _moviesLiveData

    private val _moviesLoadingStatusLiveData = Transformations.switchMap(movieDataSourceFactory.repositoryDataSourceLiveData) {
        it.loadingStatusLiveData
    }
    val moviesLoadingStatusLiveData: LiveData<LoadingStatus>
        get() = _moviesLoadingStatusLiveData

    private val _moviesNavigationLiveData = MutableLiveData<LiveDataEvent<Navigation>>()
    val moviesNavigationLiveData: LiveData<LiveDataEvent<Navigation>>
        get() = _moviesNavigationLiveData

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .build()
        _moviesLiveData = LivePagedListBuilder<Long, Movie>(movieDataSourceFactory, config).build()
    }

    fun tryAgainClick() {
        movieDataSourceFactory.repositoryDataSourceLiveData.value?.retry()
    }

    fun sortByPopularClick() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                moviesRepository.setCurrentSortingOption(SortingOption.POPULAR)
            }
            movieDataSourceFactory.repositoryDataSourceLiveData.value?.invalidate()
        }
    }

    fun sortByTopRatedClick() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                moviesRepository.setCurrentSortingOption(SortingOption.TOP_RATED)
            }
            movieDataSourceFactory.repositoryDataSourceLiveData.value?.invalidate()
        }
    }

    fun sortByUpcomingClick() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                moviesRepository.setCurrentSortingOption(SortingOption.UPCOMING)
            }
            movieDataSourceFactory.repositoryDataSourceLiveData.value?.invalidate()
        }
    }

    fun onMyFavoritesClick() {
        _moviesNavigationLiveData.value = LiveDataEvent(MyFavoritesNavigation)
    }

    fun onMovieClick(movie: Movie) {
        _moviesNavigationLiveData.value = LiveDataEvent(MovieDetailsNavigation(movie))
    }

    fun onSettingsClick() {
        _moviesNavigationLiveData.value = LiveDataEvent(SettingsNavigation)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}