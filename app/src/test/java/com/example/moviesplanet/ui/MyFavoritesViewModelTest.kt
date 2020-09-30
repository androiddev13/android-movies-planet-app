package com.example.moviesplanet.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.MoviesRepository
import com.example.data.model.Movie
import com.example.moviesplanet.getOrAwaitValue
import com.example.moviesplanet.provideTestCoroutinesDispatcherProvider
import com.example.moviesplanet.ui.favorites.MyFavoritesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MyFavoritesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var moviesRepository: MoviesRepository

    private val testFavoriteMovies = listOf(
        Movie(1, "title", "12/12/12", "url", 10.0, "overview", listOf()),
        Movie(2, "title", "12/12/12", "url", 10.0, "overview", listOf())
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun movieClicked_sendsNavigationEvent() = runBlocking {
        `when`(moviesRepository.getFavoriteMovies()).thenReturn(flowOf(listOf()))
        val movie = Movie.getEmpty()
        val viewModel = withViewModel()
        viewModel.onMovieClick(movie)
        val navigation = viewModel.favoritesNavigationLiveData.getOrAwaitValue().peekContent() as MovieDetailsNavigation
        assertSame(movie, navigation.movie)
    }

    @Test
    fun loadMovies_favoritesExist() = runBlocking {
        `when`(moviesRepository.getFavoriteMovies()).thenReturn(flowOf(testFavoriteMovies))
        val viewModel = withViewModel()
        assertEquals(testFavoriteMovies, viewModel.favoriteMoviesLiveData.getOrAwaitValue())
    }

    private fun withViewModel(): MyFavoritesViewModel {
        return MyFavoritesViewModel(moviesRepository, provideTestCoroutinesDispatcherProvider())
    }
}