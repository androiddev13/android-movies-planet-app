package com.example.moviesplanet.data

import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.data.model.MovieDetails
import com.example.moviesplanet.data.model.MovieExternalInfo
import com.example.moviesplanet.data.model.SortingOption
import com.example.moviesplanet.data.storage.remote.MoviesServiceApi
import com.example.moviesplanet.data.storage.local.AppPreferences
import com.example.moviesplanet.data.storage.local.db.MovieDao
import com.example.moviesplanet.data.storage.local.db.MovieEntity
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalStateException

class DefaultMoviesRepository constructor(private val api: MoviesServiceApi,
                                          private val appPreferences: AppPreferences,
                                          private val movieDao: MovieDao) : MoviesRepository {

    override fun getMovies(page: Int): Single<List<Movie>> {
        return api.getMovies(appPreferences.getCurrentSortingOption().sortOption, page)
            .map { response ->
                response.results
                    ?.map { it.toMovie() }
                    ?:throw IllegalStateException()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMovieDetails(movie: Movie): Single<MovieDetails> {
        return getMovieExternalInfo(movie)
            .zipWith(movieDao.getMovies(), BiFunction<List<MovieExternalInfo>, List<MovieEntity>, MovieDetails> { t1, t2 ->
                MovieDetails(movie, isFavoriteMovie(t2, movie.id), t1)
            })
            .subscribeOn(Schedulers.io())
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
        appPreferences.setCurrentSortingOption(sortingOption)
    }

    override fun addToFavorite(movie: Movie): Completable {
        return movieDao.addMovie(MovieEntity(movie.id, movie.title, movie.releaseDate, movie.posterPath, movie.voteAverage, movie.overview))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun removeFromFavorite(movie: Movie): Completable {
        return movieDao.removeMovie(MovieEntity(movie.id, movie.title, movie.releaseDate, movie.posterPath, movie.voteAverage, movie.overview))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getFavoriteMovies(): Single<List<Movie>> {
        return movieDao.getMovies()
            .map { favorites ->
                favorites.map { Movie(it.id, it.name, it.releaseDate, it.posterPath, it.voteAverage, it.overview) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getMovieReviewsExternalInfo(id: String): Single<List<MovieExternalInfo>> {
        return api.getMovieReviews(id)
            .map {
                it.results
                    ?.map { response -> response.toMovieExternalInfo() }
                    ?:throw IllegalStateException()
            }
    }

    private fun getMovieVideosExternalInfo(id: String): Single<List<MovieExternalInfo>> {
        return api.getMovieVideos(id)
            .map {
                it.results
                    ?.map { response -> response.toMovieExternalInfo() }
                    ?:throw IllegalStateException()
            }
    }

    private fun isFavoriteMovie(favorites: List<MovieEntity>, id: Int): Boolean {
        return favorites.find { it.id == id } != null
    }
}