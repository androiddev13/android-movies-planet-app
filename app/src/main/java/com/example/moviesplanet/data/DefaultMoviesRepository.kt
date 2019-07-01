package com.example.moviesplanet.data

import android.util.Log
import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.data.model.SortingOption
import com.example.moviesplanet.data.storage.MoviesResponse
import com.example.moviesplanet.data.storage.MoviesServiceApi
import com.example.moviesplanet.data.storage.local.AppPreferences
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.lang.IllegalStateException

class DefaultMoviesRepository constructor(private val api: MoviesServiceApi,
                                          private val appPreferences: AppPreferences
) : MoviesRepository {

    override fun getMovies(page: Int): Single<List<Movie>> {
        return api.listMovies(appPreferences.getCurrentSortingOption().sortOption, page)
            .map {
                it.results
                    ?.map { response -> response.toMovie() }
                    ?: throw IllegalStateException()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun setCurrentSortingOption(sortingOption: SortingOption) {
        appPreferences.setCurrentSortingOption(sortingOption)
    }
}