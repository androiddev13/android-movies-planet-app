package com.example.moviesplanet.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviesplanet.data.MoviesRepository
import com.example.moviesplanet.data.model.LoadingStatus
import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.data.model.MovieDetails
import com.example.moviesplanet.data.model.MovieExternalInfo
import com.example.moviesplanet.getOrAwaitValue
import com.example.moviesplanet.presentation.moviedetails.MovieDetailsViewModel
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when`
import java.lang.IllegalStateException

@RunWith(JUnit4::class)
class MovieDetailsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var moviesRepository: MoviesRepository

    private val testMovie = Movie(1, "title", "12/12/12", "url", 10.0, "overview", listOf())

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun setMovie_showsDetails() {
        val expected = withMovieDetails()
        `when`(moviesRepository.getMovieDetails(testMovie)).thenReturn(Single.just(expected))
        val viewModel = withViewModel()
        viewModel.setMovie(testMovie)
        assertEquals(viewModel.movieDetailsLiveData.getOrAwaitValue(), expected)
    }

    @Test
    fun setMovie_loadingFailed() {
        val expected = IllegalStateException()
        `when`(moviesRepository.getMovieDetails(testMovie)).thenReturn(Single.error(expected))
        val viewModel = withViewModel()
        viewModel.setMovie(testMovie)
        assertEquals(viewModel.loadingStatusLiveData.getOrAwaitValue().message, expected.message)
    }

    @Test
    fun tryAgainClicked_reloadsDetailsSuccess() {
        `when`(moviesRepository.getMovieDetails(Movie.getEmpty())).thenReturn(Single.just(withMovieDetails()))
        val viewModel = withViewModel().apply { setMovie(Movie.getEmpty()) }
        viewModel.onTryAgainClick()
        assertEquals(viewModel.loadingStatusLiveData.getOrAwaitValue(), LoadingStatus.LOADING_SUCCESS)
    }

    @Test
    fun externalClicked_sendsNavigationEvent() {
        val expected = MovieExternalInfo("test_name", "test_url")
        `when`(moviesRepository.getMovieDetails(testMovie)).thenReturn(Single.just(withMovieDetails()))
        val viewModel = withViewModel()
        viewModel.onExternalInfoClick(expected)
        val navigation = viewModel.navigationLiveData.getOrAwaitValue().peekContent() as ExternalWebPageNavigation
        assertEquals(navigation.url, expected.url)
    }

    @Test
    fun favoriteToggled_makesUnFavorite() {
        `when`(moviesRepository.getMovieDetails(testMovie)).thenReturn(Single.just(withMovieDetails(true)))
        `when`(moviesRepository.removeFromFavorite(testMovie)).thenReturn(Completable.complete())
        val viewModel = withViewModel().apply { setMovie(testMovie) }
        viewModel.toggleFavMovie()
        assertFalse(viewModel.movieDetailsLiveData.getOrAwaitValue().isFavorite)
    }

    @Test
    fun unFavoriteToggled_makesFavorite() {
        `when`(moviesRepository.getMovieDetails(testMovie)).thenReturn(Single.just(withMovieDetails(false)))
        `when`(moviesRepository.addToFavorite(testMovie)).thenReturn(Completable.complete())
        val viewModel = withViewModel().apply { setMovie(testMovie) }
        viewModel.toggleFavMovie()
        assertTrue(viewModel.movieDetailsLiveData.getOrAwaitValue().isFavorite)
    }

    private fun withViewModel(): MovieDetailsViewModel {
        return MovieDetailsViewModel(moviesRepository)
    }

    private fun withMovieDetails(isFavorite: Boolean = false): MovieDetails {
        return MovieDetails(testMovie, isFavorite, listOf(), listOf())
    }
}