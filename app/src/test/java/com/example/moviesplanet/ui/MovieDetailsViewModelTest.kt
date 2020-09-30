package com.example.moviesplanet.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.MoviesRepository
import com.example.data.model.*
import com.example.moviesplanet.getOrAwaitValue
import com.example.moviesplanet.provideTestCoroutinesDispatcherProvider
import com.example.moviesplanet.ui.moviedetails.MovieDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
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
    fun setMovie_showsDetails() = runBlocking {
        val expected = withMovieDetails()
        `when`(moviesRepository.getMovieDetails(testMovie)).thenReturn(Result.Success(expected))
        val viewModel = withViewModel()
        viewModel.setMovie(testMovie)
        assertEquals(expected, viewModel.movieDetailsLiveData.getOrAwaitValue())
    }

    @Test
    fun setMovie_loadingFailed() = runBlocking {
        `when`(moviesRepository.getMovieDetails(testMovie)).thenReturn(Result.Error(IllegalStateException()))
        val viewModel = withViewModel()
        viewModel.setMovie(testMovie)
        assertEquals(Status.LOADING_FAILED, viewModel.loadingStatusLiveData.getOrAwaitValue().status)
    }

    @Test
    fun tryAgainClicked_reloadsDetailsSuccess() = runBlocking {
        `when`(moviesRepository.getMovieDetails(Movie.getEmpty())).thenReturn(Result.Success(withMovieDetails()))
        val viewModel = withViewModel().apply { setMovie(Movie.getEmpty()) }
        viewModel.onTryAgainClick()
        assertEquals(LoadingStatus.LOADING_SUCCESS, viewModel.loadingStatusLiveData.getOrAwaitValue())
    }

    @Test
    fun externalClicked_sendsNavigationEvent() = runBlocking {
        val expected = MovieExternalInfo("test_name", "test_url")
        `when`(moviesRepository.getMovieDetails(testMovie)).thenReturn(Result.Success(withMovieDetails()))
        val viewModel = withViewModel()
        viewModel.onExternalInfoClick(expected)
        val navigation = viewModel.navigationLiveData.getOrAwaitValue().peekContent() as ExternalWebPageNavigation
        assertEquals(expected.url, navigation.url)
    }

    @Test
    fun favoriteToggled_makesUnFavorite() = runBlocking {
        `when`(moviesRepository.getMovieDetails(testMovie)).thenReturn(Result.Success(withMovieDetails(true)))
        `when`(moviesRepository.removeFromFavorite(testMovie)).thenReturn(Unit)
        val viewModel = withViewModel().apply { setMovie(testMovie) }
        viewModel.toggleFavMovie()
        assertFalse(viewModel.movieDetailsLiveData.getOrAwaitValue().isFavorite)
    }

    @Test
    fun unFavoriteToggled_makesFavorite() = runBlocking {
        `when`(moviesRepository.getMovieDetails(testMovie)).thenReturn(Result.Success(withMovieDetails(false)))
        `when`(moviesRepository.addToFavorite(testMovie)).thenReturn(Unit)
        val viewModel = withViewModel().apply { setMovie(testMovie) }
        viewModel.toggleFavMovie()
        assertTrue(viewModel.movieDetailsLiveData.getOrAwaitValue().isFavorite)
    }

    private fun withViewModel(): MovieDetailsViewModel {
        return MovieDetailsViewModel(moviesRepository, provideTestCoroutinesDispatcherProvider())
    }

    private fun withMovieDetails(isFavorite: Boolean = false): MovieDetails {
        return MovieDetails(testMovie, isFavorite, listOf(), listOf())
    }
}