package com.example.moviesplanet.data

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class DefaultMoviesRepository constructor(private val api: MoviesServiceApi) : MoviesRepository {

    override fun getMovies(page: Int): Single<List<MoviesResponse>> {
        return api.listMovies("popular")
            .map { it.results?:throw Exception()}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}