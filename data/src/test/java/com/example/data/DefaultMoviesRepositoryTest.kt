package com.example.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.model.MovieDetails
import com.example.data.model.Result
import com.example.data.storage.local.MoviesLocalDataSource
import com.example.data.storage.remote.MoviesRemoteDataSource
import com.example.data.storage.remote.model.GenreListResponse
import com.example.data.storage.remote.model.MovieListResponse
import com.example.data.storage.remote.model.MoviesResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.`when`
import java.io.IOException
import java.lang.IllegalStateException

@RunWith(JUnit4::class)
class DefaultMoviesRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var remoteDataSource: MoviesRemoteDataSource

    @Mock
    lateinit var localDataSource: MoviesLocalDataSource

    private lateinit var defaultMoviesRepository: DefaultMoviesRepository

    private val testMovieListResponse = MovieListResponse(1, listOf(testMovieResponse(1), testMovieResponse(2), testMovieResponse(3)), 3, 1)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        defaultMoviesRepository = DefaultMoviesRepository(remoteDataSource, localDataSource)
    }

    @Test
    fun getMovies_withSuccess() = runBlocking {
        val expected = Result.Success(testMovieListResponse.results?.map { it.toMovie() }?: listOf())
        `when`(remoteDataSource.getMovies(anyLong())).thenReturn(Result.Success(testMovieListResponse))
        val result = defaultMoviesRepository.getMovies(anyLong())
        assertEquals(expected, result)
    }

    @Test
    fun getMovies_withError() = runBlocking {
        val expected = Result.Error(IOException())
        `when`(remoteDataSource.getMovies(anyLong())).thenReturn(expected)
        val result = defaultMoviesRepository.getMovies(anyLong())
        assertEquals(expected, result)
    }

    @Test
    fun getMovieDetails_isNotFavorite_withSuccess() = runBlocking {
        `when`(localDataSource.getFavoritesMovie()).thenReturn(flowOf(listOf()))
        `when`(remoteDataSource.getMovieGenres()).thenReturn(Result.Success(GenreListResponse.getEmpty()))
        `when`(remoteDataSource.getMovieReviews(anyString())).thenReturn(Result.Success(reviewListResponse))
        `when`(remoteDataSource.getMovieVideos(anyString())).thenReturn(Result.Success(videoListResponse))
        val expected = Result.Success(MovieDetails(
            movie = movie1,
            isFavorite = false,
            externalInfo = (reviewListResponse.results?.map { it.toMovieExternalInfo() }?: listOf()) + (videoListResponse.results?.map { it.toMovieExternalInfo() }?: listOf()),
            movieGenres = listOf()
        ))
        // When requesting details for a movie which isn't favorite.
        val result = defaultMoviesRepository.getMovieDetails(movie1)
        // Then the correct result is returned.
        assertEquals(expected, result)
    }

    @Test
    fun getMovieDetails_isFavorite_withSuccess() = runBlocking {
        `when`(localDataSource.getFavoritesMovie()).thenReturn(flowOf(listOf(movie1, movie2)))
        `when`(remoteDataSource.getMovieGenres()).thenReturn(Result.Success(GenreListResponse.getEmpty()))
        `when`(remoteDataSource.getMovieReviews(anyString())).thenReturn(Result.Success(reviewListResponse))
        `when`(remoteDataSource.getMovieVideos(anyString())).thenReturn(Result.Success(videoListResponse))
        val expected = Result.Success(MovieDetails(
            movie = movie1,
            isFavorite = true,
            externalInfo = (reviewListResponse.results?.map { it.toMovieExternalInfo() }?: listOf()) + (videoListResponse.results?.map { it.toMovieExternalInfo() }?: listOf()),
            movieGenres = listOf()
        ))
        // When requesting details for a movie which is favorite.
        val result = defaultMoviesRepository.getMovieDetails(movie1)
        // Then the correct result is returned.
        assertEquals(expected, result)
    }

    @Test
    fun getMovieDetails_withError() = runBlocking {
        // Given that a request for movie's reviews has been failed.
        val expected = IllegalStateException("message")
        `when`(localDataSource.getFavoritesMovie()).thenReturn(flowOf(listOf()))
        `when`(remoteDataSource.getMovieGenres()).thenReturn(Result.Success(GenreListResponse.getEmpty()))
        `when`(remoteDataSource.getMovieReviews(anyString())).thenReturn(Result.Error(expected))
        `when`(remoteDataSource.getMovieVideos(anyString())).thenReturn(Result.Success(videoListResponse))
        // When requesting details for a movie.
        val result = defaultMoviesRepository.getMovieDetails(movie1)
        // Then the correct result is returned.
        assertEquals(Result.Error(expected), result)
    }

    @Test
    fun getFavoriteMovies_withSuccess() = runBlocking {
        // Given that a user has favorite movies.
        `when`(localDataSource.getFavoritesMovie()).thenReturn(flowOf(movies))
        // When favorite movies are requested.
        val result = defaultMoviesRepository.getFavoriteMovies()
        // Then the correct result is returned.
        assertEquals(movies, result.first())
    }

    private fun testMovieResponse(id: Int): MoviesResponse {
        return MoviesResponse("", true, "", "", listOf(), id, "", "", "", "", 0.0, 1, true, 0.0)
    }
}