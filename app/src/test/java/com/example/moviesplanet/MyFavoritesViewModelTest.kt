package com.example.moviesplanet

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviesplanet.data.MoviesRepository
import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.presentation.MovieDetailsNavigation
import com.example.moviesplanet.presentation.favorites.MyFavoritesViewModel
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.lang.IllegalStateException

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
    fun movieClicked_sendsNavigationEvent() {
        `when`(moviesRepository.getFavoriteMovies()).thenReturn(Observable.just(listOf()))
        val movie = Movie.getEmpty()
        val viewModel = withViewModel()
        viewModel.onMovieClick(movie)
        val navigation = viewModel.favoritesNavigationLiveData.getOrAwaitValue().peekContent() as MovieDetailsNavigation
        assertSame(navigation.movie, movie)
    }

    @Test
    fun loadMovies_favoritesExist() {
        `when`(moviesRepository.getFavoriteMovies()).thenReturn(Observable.fromArray(testFavoriteMovies))
        val viewModel = withViewModel()
        assertEquals(viewModel.favoriteMoviesLiveData.getOrAwaitValue(), testFavoriteMovies)
    }

    @Test
    fun loadMovies_somethingFailed() {
        val exception = IllegalStateException()
        `when`(moviesRepository.getFavoriteMovies()).thenReturn(Observable.error(exception))
        val viewModel = withViewModel()
        assertEquals(viewModel.favoriteMoviesLiveData.getOrAwaitValue(), listOf<Movie>())
    }

    private fun withViewModel(): MyFavoritesViewModel {
        return MyFavoritesViewModel(moviesRepository)
    }
}