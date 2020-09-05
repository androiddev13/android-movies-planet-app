package com.example.moviesplanet.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviesplanet.data.model.SortingOption
import com.example.moviesplanet.data.storage.local.MoviesLocalDataSource
import com.example.moviesplanet.data.storage.remote.MoviesRemoteDataSource
import com.example.moviesplanet.data.storage.remote.MoviesServiceApi
import com.example.moviesplanet.data.storage.remote.model.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when`

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
    fun getMovies_successful() {
        val expected = MovieListResponse(1, listOf(), 1, 1)
        `when`(moviesServiceApi.getMovies(anyString(), anyLong())).thenReturn(Single.just(expected))
        `when`(moviesLocalDataSource.getSortingOption()).thenReturn(SortingOption.POPULAR)
        val testObserver = moviesRemoteDataSource.getMovies(1).test()
        testObserver.assertValue(expected).dispose()
    }

    @Test
    fun getMovies_failed() {
        val expected = Exception()
        `when`(moviesServiceApi.getMovies(anyString(), anyLong())).thenReturn(Single.error(expected))
        `when`(moviesLocalDataSource.getSortingOption()).thenReturn(SortingOption.POPULAR)
        val testObserver = moviesRemoteDataSource.getMovies(1).test()
        testObserver.assertError(expected).dispose()
    }

    @Test
    fun getMovieVideos_successful() {
        val expected = VideoListResponse(1, null)
        `when`(moviesServiceApi.getMovieVideos(anyString())).thenReturn(Single.just(expected))
        val testObserver = moviesRemoteDataSource.getMovieVideos(anyString()).test()
        testObserver.assertValue(expected).dispose()
    }

    @Test
    fun getMovieVideos_failed() {
        val expected = Exception()
        `when`(moviesServiceApi.getMovieVideos(anyString())).thenReturn(Single.error(expected))
        val testObserver = moviesRemoteDataSource.getMovieVideos(anyString()).test()
        testObserver.assertError(expected).dispose()
    }

    @Test
    fun getMovieReviews_successful() {
        val expected = ReviewListResponse(1, listOf(ReviewResponse("id", "author", "content", "url")))
        `when`(moviesServiceApi.getMovieReviews(anyString())).thenReturn(Single.just(expected))
        val testObserver =  moviesRemoteDataSource.getMovieReviews(anyString()).test()
        testObserver.assertValue(expected).dispose()
    }

    @Test
    fun getMovieReviews_failed() {
        val expected = Exception()
        `when`(moviesServiceApi.getMovieReviews(anyString())).thenReturn(Single.error(expected))
        val testObserver = moviesRemoteDataSource.getMovieReviews(anyString()).test()
        testObserver.assertError(expected).dispose()
    }

    @Test
    fun getGenres_successful() {
        val expected = GenreListResponse(listOf(GenreResponse(1, "name")))
        `when`(moviesServiceApi.getMovieGenres()).thenReturn(Single.just(expected))
        val testObserver = moviesRemoteDataSource.getGenres().test()
        testObserver.assertValue(expected).dispose()
    }

    @Test
    fun getGenres_failed() {
        val expected = Exception()
        `when`(moviesServiceApi.getMovieGenres()).thenReturn(Single.error(expected))
        val testObserver = moviesRemoteDataSource.getGenres().test()
        testObserver.assertError(expected)
    }
}