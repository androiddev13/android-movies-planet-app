package com.example.moviesplanet

import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.example.moviesplanet.data.MoviesDataSourceFactory
import com.example.moviesplanet.data.MoviesRepository
import com.example.moviesplanet.data.model.LoadingStatus
import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.presentation.MovieDetailsNavigation
import com.example.moviesplanet.presentation.MyFavoritesNavigation
import com.example.moviesplanet.presentation.Navigation
import com.example.moviesplanet.presentation.SettingsNavigation
import com.example.moviesplanet.presentation.generic.LiveDataEventObserver
import com.example.moviesplanet.presentation.movies.MoviesViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class MovieViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var moviesRepository: MoviesRepository

    @Mock
    lateinit var moviesDataSourceFactory: MoviesDataSourceFactory

    @Mock
    lateinit var navigationObserver: LiveDataEventObserver<Navigation>

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MoviesViewModel(moviesRepository, moviesDataSourceFactory)
        viewModel.moviesNavigationLiveData.observeForever(navigationObserver)
    }

    @Test
    fun testMyFavoritesClick() {
        viewModel.onMyFavoritesClick()
        assertEquals(viewModel.moviesNavigationLiveData.value?.peekContent(), MyFavoritesNavigation)
    }

    @Test
    fun testMovieClick() {
        val movie = Movie.getEmpty()
        viewModel.onMovieClick(movie)
        assertSame((viewModel.moviesNavigationLiveData.value?.peekContent() as MovieDetailsNavigation).movie, movie)
    }

    @Test
    fun testSettingClick() {
        viewModel.onSettingsClick()
        assertEquals(viewModel.moviesNavigationLiveData.value?.peekContent(), SettingsNavigation)
    }
}