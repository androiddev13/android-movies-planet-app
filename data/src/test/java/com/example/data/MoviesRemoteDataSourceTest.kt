package com.example.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.model.Result
import com.example.data.model.SortingOption
import com.example.data.storage.local.MoviesLocalDataSource
import com.example.data.storage.remote.MoviesRemoteDataSource
import com.example.data.storage.remote.MoviesServiceApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when`
import retrofit2.Response

@RunWith(JUnit4::class)
class MoviesRemoteDataSourceTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var moviesServiceApi: MoviesServiceApi

    @Mock
    lateinit var moviesLocalDataSource: MoviesLocalDataSource

    lateinit var moviesRemoteDataSource: MoviesRemoteDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        moviesRemoteDataSource = MoviesRemoteDataSource(moviesServiceApi, moviesLocalDataSource)
    }

    @Test
    fun getMovies_withSuccess() = runBlocking {
        // Given that api returns movies.
        `when`(moviesServiceApi.getMovies(anyString(), anyLong())).thenReturn(Response.success(movieListResponse))
        `when`(moviesLocalDataSource.getSortingOption()).thenReturn(SortingOption.POPULAR)
        // When requesting movies.
        val result = moviesRemoteDataSource.getMovies(1)
        // Then the correct result is returned.
        assertEquals(Result.Success(movieListResponse), result)
    }

    @Test
    fun getMovies_withError() = runBlocking {
        // Given that api responds with failure.
        `when`(moviesServiceApi.getMovies(anyString(), anyLong())).thenReturn(Response.error(404, ResponseBody.create(null, "error")))
        `when`(moviesLocalDataSource.getSortingOption()).thenReturn(SortingOption.POPULAR)
        // When requesting movies.
        val result = moviesRemoteDataSource.getMovies(1)
        // Then the result is error.
        assert(result is Result.Error)
    }

    @Test
    fun getMovieVideos_withSuccess() = runBlocking {
        // Given that api returns movie videos.
        `when`(moviesServiceApi.getMovieVideos(anyString())).thenReturn(Response.success(videoListResponse))
        // When requesting movie videos.
        val result = moviesRemoteDataSource.getMovieVideos(anyString())
        // Then the correct result is returned.
        assertEquals(Result.Success(videoListResponse), result)
    }

    @Test
    fun getMovieVideos_withError() = runBlocking {
        // Given that api responds with failure.
        `when`(moviesServiceApi.getMovieVideos(anyString())).thenReturn(Response.error(404, ResponseBody.create(null, "error")))
        // When requesting movie videos.
        val result = moviesRemoteDataSource.getMovieVideos(anyString())
        // Then the result is error.
        assert(result is Result.Error)
    }

    @Test
    fun getMovieReviews_withSuccess() = runBlocking {
        // Given that api return movie reviews.
        `when`(moviesServiceApi.getMovieReviews(anyString())).thenReturn(Response.success(reviewListResponse))
        // When requesting movie reviews.
        val result =  moviesRemoteDataSource.getMovieReviews(anyString())
        // Then the correct result is returned.
        assertEquals(Result.Success(reviewListResponse), result)
    }

    @Test
    fun getMovieReviews_withError() = runBlocking {
        // Given that api responds with failure.
        `when`(moviesServiceApi.getMovieReviews(anyString())).thenReturn(Response.error(404, ResponseBody.create(null, "error")))
        // When requesting movie reviews.
        val result = moviesRemoteDataSource.getMovieReviews(anyString())
        // Then the result is error.
        assert(result is Result.Error)
    }

    @Test
    fun getMovieGenres_withSuccess() = runBlocking {
        // Given that api returns list of genres.
        `when`(moviesServiceApi.getMovieGenres()).thenReturn(Response.success(genreListResponse))
        // When requesting genres.
        val result = moviesRemoteDataSource.getMovieGenres()
        // Then the correct result is returned.
        assertEquals(Result.Success(genreListResponse), result)
    }

    @Test
    fun getMovieGenres_withError() = runBlocking {
        // Given that api responds with failure.
        `when`(moviesServiceApi.getMovieGenres()).thenReturn(Response.error(404, ResponseBody.create(null, "error")))
        // When requesting genres.
        val result = moviesRemoteDataSource.getMovieGenres()
        // Then the result is error.
        assert(result is Result.Error)
    }
}