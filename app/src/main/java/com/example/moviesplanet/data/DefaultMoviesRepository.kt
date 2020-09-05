package com.example.moviesplanet.data

import com.example.moviesplanet.data.model.*
import com.example.moviesplanet.data.storage.local.MoviesLocalDataSource
import com.example.moviesplanet.data.storage.local.db.*
import com.example.moviesplanet.data.storage.remote.MoviesRemoteDataSource
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalStateException

class DefaultMoviesRepository constructor(private val remoteDataSource: MoviesRemoteDataSource,
                                          private val localDataSource: MoviesLocalDataSource,
                                          private val movieDao: MovieDao) : MoviesRepository {

    /**
     * In-memory cache for list of [MovieGenre] to avoid fetching it each time movie details is
     * required during application lifecycle.
     */
    private var genres = listOf<MovieGenre>()

    override fun getMovies(page: Long): Single<List<Movie>> {
        return remoteDataSource.getMovies(page)
            .map { response ->
                response.results
                    ?.map { it.toMovie() }
                    ?:throw IllegalStateException()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMovieDetails(movie: Movie): Single<MovieDetails> {
        return Single.zip(getMovieExternalInfo(movie), movieDao.getMoviesWithGenres().first(listOf()), getMovieGenres(movie), Function3<List<MovieExternalInfo>, List<MovieWithGenres>, List<MovieGenre>, MovieDetails> { t1, t2, t3 ->
                MovieDetails(movie, isFavoriteMovie(t2, movie.id), t1, t3)
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getMovieExternalInfo(movie: Movie): Single<List<MovieExternalInfo>> {
        val id = movie.id.toString()
        return getMovieReviewsExternalInfo(id)
            .zipWith(getMovieVideosExternalInfo(id),
                BiFunction<List<MovieExternalInfo>, List<MovieExternalInfo>,  List<List<MovieExternalInfo>>> { t1, t2 -> listOf(t1, t2) })
            .map { it.flatten() }
    }

    override fun setCurrentSortingOption(sortingOption: SortingOption) {
        localDataSource.setSortingOption(sortingOption)
    }

    override fun addToFavorite(movie: Movie): Completable {
        return movieDao.addMovie(MovieEntity.from(movie))
            .andThen(movieDao.addMovieGenres(movie.genres.map { MovieGenreCrossRef(movie.id, it) }))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun removeFromFavorite(movie: Movie): Completable {
        return movieDao.removeMovie(MovieEntity.from(movie))
            .andThen(movieDao.removeMovieGenre(movie.id))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getFavoriteMovies(): Observable<List<Movie>> {
        return movieDao.getMoviesWithGenres()
            .map { favorites ->
                favorites.map { Movie(it.movie.movieId, it.movie.name, it.movie.releaseDate, it.movie.posterPath, it.movie.voteAverage, it.movie.overview, it.genres.map { genre -> genre.genreId }) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getMovieReviewsExternalInfo(id: String): Single<List<MovieExternalInfo>> {
        return remoteDataSource.getMovieReviews(id)
            .map {
                it.results
                    ?.map { response -> response.toMovieExternalInfo() }
                    ?:throw IllegalStateException()
            }
    }

    private fun getMovieVideosExternalInfo(id: String): Single<List<MovieExternalInfo>> {
        return remoteDataSource.getMovieVideos(id)
            .map {
                it.results
                    ?.map { response -> response.toMovieExternalInfo() }
                    ?:throw IllegalStateException()
            }
    }

    private fun isFavoriteMovie(favorites: List<MovieWithGenres>, id: Int): Boolean {
        return favorites.find { it.movie.movieId == id } != null
    }

    private fun getMovieGenres(movie: Movie): Single<List<MovieGenre>> {
        val source = if (genres.isEmpty()) getGenres() else Single.just(genres)
        return source.map {
            it.filter { genre -> movie.genres.contains(genre.id) }
        }
    }

    private fun getGenres(): Single<List<MovieGenre>> {
        return remoteDataSource.getGenres()
            .map { response ->
                genres = response.genres?.map { MovieGenre(it.id, it.name) }?: listOf()
                genres
            }
            .flatMap { genres ->
                movieDao.addGenres(genres.map { GenreEntity(it.id, it.name) }).andThen(Single.just(genres))
            }
    }
}