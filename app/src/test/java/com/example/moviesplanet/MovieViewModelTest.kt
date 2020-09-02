package com.example.moviesplanet

import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviesplanet.data.MoviesDataSourceFactory
import com.example.moviesplanet.data.MoviesRepository
import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.presentation.MovieDetailsNavigation
import com.example.moviesplanet.presentation.MyFavoritesNavigation
import com.example.moviesplanet.presentation.SettingsNavigation
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

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MoviesViewModel(moviesRepository, moviesDataSourceFactory)
    }

    @Test
    fun favoritesClicked_sendsNavigationEvent() {
        viewModel.onMyFavoritesClick()
        val navigation = viewModel.moviesNavigationLiveData.getOrAwaitValue().peekContent()
        assertEquals(navigation, MyFavoritesNavigation)
    }

    @Test
    fun movieClicked_sendsNavigationEvent() {
        val movie = Movie.getEmpty()
        viewModel.onMovieClick(movie)
        val navigation = viewModel.moviesNavigationLiveData.getOrAwaitValue().peekContent() as MovieDetailsNavigation
        assertSame(navigation.movie, movie)
    }

    @Test
    fun settingsClicked_sendsNavigationEvent() {
        viewModel.onSettingsClick()
        val navigation = viewModel.moviesNavigationLiveData.getOrAwaitValue().peekContent()
        assertEquals(navigation, SettingsNavigation)
    }
}