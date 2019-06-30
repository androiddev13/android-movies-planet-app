package com.example.moviesplanet.data

import io.reactivex.Single

interface MoviesRepository {

    fun getMovies(page: Int): Single<List<MoviesResponse>>

    fun setCurrentSortingOption(sortingOption: SortingOption)

}