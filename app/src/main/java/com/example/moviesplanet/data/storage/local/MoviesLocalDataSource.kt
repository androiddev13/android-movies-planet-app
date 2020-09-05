package com.example.moviesplanet.data.storage.local

import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.data.model.MovieGenre
import com.example.moviesplanet.data.model.SortingOption
import com.example.moviesplanet.data.storage.local.db.GenreEntity
import com.example.moviesplanet.data.storage.local.db.MovieDao
import com.example.moviesplanet.data.storage.local.db.MovieEntity
import com.example.moviesplanet.data.storage.local.db.MovieGenreCrossRef
import io.reactivex.Completable
import io.reactivex.Observable

class MoviesLocalDataSource constructor(private val moviesPreferences: MoviesPreferences,
                                        private val movieDao: MovieDao) {

    fun getFavoritesMovie(): Observable<List<Movie>> {
        return movieDao.getMoviesWithGenres()
            .map { favorites ->
                favorites.map {
                    Movie(it.movie.movieId, it.movie.name, it.movie.releaseDate, it.movie.posterPath, it.movie.voteAverage, it.movie.overview, it.genres.map { genre -> genre.genreId }) }
            }
    }

    fun addMovieToFavorites(movie: Movie): Completable {
        return movieDao.addMovie(MovieEntity.from(movie))
            .andThen(addMovieGenres(movie.id, movie.genres))
    }

    fun removeMovieFromFavorites(movie: Movie): Completable {
        return movieDao.removeMovie(MovieEntity.from(movie))
            .andThen(removeMovieGenre(movie.id))
    }

    fun setSortingOption(sortingOption: SortingOption) {
        moviesPreferences.setCurrentSortingOption(sortingOption)
    }

    fun getSortingOption(): SortingOption {
        return moviesPreferences.getCurrentSortingOption()
    }

    fun addGenres(genres: List<MovieGenre>): Completable {
        return movieDao.addGenres(genres.map { GenreEntity(it.id, it.name) })
    }

    private fun removeMovieGenre(movieId: Int): Completable {
        return movieDao.removeMovieGenre(movieId)
    }

    private fun addMovieGenres(movieId: Int, genreIds: List<Int>): Completable {
        return movieDao.addMovieGenres(genreIds.map { MovieGenreCrossRef(movieId, it) })
    }
}