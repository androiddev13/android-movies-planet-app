package com.example.moviesplanet.presentation.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviesplanet.data.MoviesDataSourceFactory
import com.example.moviesplanet.data.MoviesRepository
import com.example.moviesplanet.data.PagingLoadingStatus
import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.data.model.SortingOption
import com.example.moviesplanet.presentation.generic.LiveDataEvent
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository,
                                          private val movieDataSourceFactory: MoviesDataSourceFactory) : ViewModel() {

    private val _moviesLiveData: LiveData<PagedList<Movie>>
    val moviesLiveData: LiveData<PagedList<Movie>>
        get() = _moviesLiveData

    private val _moviesLoadingStatusLiveData = Transformations.switchMap(movieDataSourceFactory.repositoryDataSourceLiveData) {
        it.loadingStatusLiveData
    }
    val moviesLoadingStatusLiveData: LiveData<PagingLoadingStatus>
        get() = _moviesLoadingStatusLiveData

    private val _moviesNavigationLiveData = MutableLiveData<LiveDataEvent<MoviesNavigation>>()
    val moviesNavigationLiveData: LiveData<LiveDataEvent<MoviesNavigation>>
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
        moviesRepository.setCurrentSortingOption(SortingOption.POPULAR)
        movieDataSourceFactory.repositoryDataSourceLiveData.value?.invalidate()
    }

    fun sortByTopRatedClick() {
        moviesRepository.setCurrentSortingOption(SortingOption.TOP_RATED)
        movieDataSourceFactory.repositoryDataSourceLiveData.value?.invalidate()
    }

    fun sortByUpcomingClick() {
        moviesRepository.setCurrentSortingOption(SortingOption.UPCOMING)
        movieDataSourceFactory.repositoryDataSourceLiveData.value?.invalidate()
    }

    fun onMyFavoritesClick() {
        _moviesNavigationLiveData.value = LiveDataEvent(MoviesNavigation.toMyFavorites())
    }

    fun onMovieClick(movie: Movie) {
        _moviesNavigationLiveData.value = LiveDataEvent(MoviesNavigation.toMovieDetails(movie))
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}