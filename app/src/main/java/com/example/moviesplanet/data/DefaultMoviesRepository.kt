package com.example.moviesplanet.data

import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.data.model.MovieExternalInfo
import com.example.moviesplanet.data.model.SortingOption
import com.example.moviesplanet.data.storage.remote.MoviesServiceApi
import com.example.moviesplanet.data.storage.local.AppPreferences
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalStateException

class DefaultMoviesRepository constructor(private val api: MoviesServiceApi,
                                          private val appPreferences: AppPreferences
) : MoviesRepository {

    override fun getMovies(page: Int): Single<List<Movie>> {
        return api.getMovies(appPreferences.getCurrentSortingOption().sortOption, page)
            .map {
                it.results
                    ?. map { response -> response.toMovie() }
                    ?: throw IllegalStateException()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMovieExternalInfo(movie: Movie): Single<List<MovieExternalInfo>> {
        val id = movie.id.toString()
        return getMovieReviewsExternalInfo(id)
            .zipWith(getMovieVideosExternalInfo(id),
                BiFunction<List<MovieExternalInfo>, List<MovieExternalInfo>,  List<List<MovieExternalInfo>>> { t1, t2 -> listOf(t1, t2) })
            .map { it.flatten() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun setCurrentSortingOption(sortingOption: SortingOption) {
        appPreferences.setCurrentSortingOption(sortingOption)
    }

    private fun getMovieReviewsExternalInfo(id: String): Single<List<MovieExternalInfo>> {
        return api.getMovieReviews(id)
            .map {
                it.results
                    ?. map { response -> response.toMovieExternalInfo() }
                    ?: throw IllegalStateException()
            }
    }

    private fun getMovieVideosExternalInfo(id: String): Single<List<MovieExternalInfo>> {
        return api.getMovieVideos(id)
            .map {
                it.results
                    ?. map { response -> response.toMovieExternalInfo() }
                    ?: throw IllegalStateException()
            }
    }
}