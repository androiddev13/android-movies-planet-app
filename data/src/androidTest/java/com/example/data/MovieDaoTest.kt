package com.example.data

import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.data.storage.local.db.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class MovieDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var movieDatabase: MovieRoomDatabase

    private lateinit var movieDao: MovieDao

    private val testMovieEntity = MovieEntity(1, "name", "12/12/12", "url", 10.0, "overview")

    private val testGenres = listOf(GenreEntity(1, "name1"), GenreEntity(2, "name2"), GenreEntity(3, "name3"))

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        movieDatabase = Room.inMemoryDatabaseBuilder(context, MovieRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        movieDao = movieDatabase.movieDao()
    }

    @Test
    fun addMovie() = runBlocking {
        // Given a movie which has been inserted in the database.
        movieDao.addMovie(testMovieEntity)

        // When getting all movies.
        val result = movieDao.getMoviesWithGenres().first()

        // Then the resulted list contains only one item which is the same as the inserted.
        assertEquals(1, result.size)
        assertEquals(testMovieEntity, result.first().movie)
    }

    @Test
    fun replaceMovie() = runBlocking {
        // Given a movie which has been inserted in the database.
        movieDao.addMovie(testMovieEntity)

        // When a movie with updated name is inserted.
        val updatedName = "updated name"
        val updatedMovie = testMovieEntity.copy(name = updatedName)
        movieDao.addMovie(updatedMovie)
        val result = movieDao.getMoviesWithGenres().first()

        // Then the resulted list contains a movie with updated title.
        assertEquals(updatedName, result.first().movie.name)
    }

    @Test
    fun removeMovie() = runBlocking {
        // Given a movie which has been inserted in the database.
        movieDao.addMovie(testMovieEntity)

        // When the movie is deleted.
        movieDao.removeMovie(testMovieEntity)
        val result = movieDao.getMoviesWithGenres().first()

        // Then no movies in the database.
        assertTrue(result.isEmpty())
    }

    @Test
    fun addMovieGenres() = runBlocking {
        // Given a movie and a list of genres that has been inserted in the database.
        movieDao.addMovie(testMovieEntity)
        movieDao.addGenres(testGenres)

        // When one genre for the movie is added.
        movieDao.addMovieGenres(listOf(MovieGenreCrossRef(testMovieEntity.movieId, testGenres.first().genreId)))
        val result = movieDao.getMoviesWithGenres().first()

        // Then the resulted list contains the movie with only one genre which was added.
        assertEquals(1, result.first().genres.size)
        assertEquals(testGenres.first().genreId, testGenres.first().genreId)
    }

    @Test
    fun removeMovieGenres() = runBlocking {
        // Given a movie with list of one genre.
        movieDao.addMovie(testMovieEntity)
        movieDao.addGenres(testGenres)
        movieDao.addMovieGenres(listOf(MovieGenreCrossRef(testMovieEntity.movieId, testGenres.first().genreId)))

        // When the genres for the movie are removed.
        movieDao.removeMovieGenres(testMovieEntity.movieId)
        val result = movieDao.getMoviesWithGenres().first()

        // Then the movie doesn't have any genres.
        assertTrue(result.first().genres.isEmpty())
    }

    @Test
    fun getMoviesWithGenres() = runBlocking {
        // Given three movies that has been inserted.
        movieDao.addMovie(testMovieEntity)
        movieDao.addMovie(testMovieEntity.copy(movieId = 2))
        movieDao.addMovie(testMovieEntity.copy(movieId = 3))

        // When movies is requested.
        val result = movieDao.getMoviesWithGenres().first()

        // Then the resulted list contains three items.
        assertEquals(3, result.size)
    }

    @After
    @Throws(IOException::class)
    fun clean() {
        movieDatabase.close()
    }
}