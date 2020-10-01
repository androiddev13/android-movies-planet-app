package com.example.data.storage.local

import com.example.data.model.Movie
import com.example.data.model.MovieGenre
import com.example.data.model.SortingOption
import com.example.data.storage.local.db.GenreEntity
import com.example.data.storage.local.db.MovieDao
import com.example.data.storage.local.db.MovieEntity
import com.example.data.storage.local.db.MovieGenreCrossRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesLocalDataSource constructor(private val moviesPreferences: MoviesPreferences,
                                        private val movieDao: MovieDao) {

    fun getFavoritesMovie(): Flow<List<Movie>> {
        return movieDao.getMoviesWithGenres().map { favorites ->
            favorites.map {
                Movie(it.movie.movieId, it.movie.name, it.movie.releaseDate, it.movie.posterPath, it.movie.voteAverage, it.movie.overview, it.genres.map { genre -> genre.genreId })
            }
        }
    }

    suspend fun addMovieToFavorites(movie: Movie) {
        movieDao.addMovie(MovieEntity.from(movie))
        movieDao.addMovieGenres(movie.genres.map { MovieGenreCrossRef(movie.id, it) })
    }

    suspend fun removeMovieFromFavorites(movie: Movie) {
        movieDao.removeMovie(MovieEntity.from(movie))
        movieDao.removeMovieGenres(movie.id)
    }

    fun setSortingOption(sortingOption: SortingOption) {
        moviesPreferences.setCurrentSortingOption(sortingOption)
    }

    fun getSortingOption(): SortingOption {
        return moviesPreferences.getCurrentSortingOption()
    }

    suspend fun addGenres(genres: List<MovieGenre>) {
        movieDao.addGenres(genres.map { GenreEntity(it.id, it.name) })
    }
}