package com.example.moviesplanet.data

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class DefaultMoviesRepository constructor(private val api: MoviesServiceApi,
                                          private val appPreferences: AppPreferences) : MoviesRepository {

    override fun getMovies(page: Int): Single<List<MoviesResponse>> {
        return api.listMovies(appPreferences.getCurrentSortingOption().sortOption, page)
            .map { it.results?:throw Exception()}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun setCurrentSortingOption(sortingOption: SortingOption) {
        appPreferences.setCurrentSortingOption(sortingOption)
    }
}