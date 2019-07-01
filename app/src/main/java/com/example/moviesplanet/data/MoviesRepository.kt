package com.example.moviesplanet.data

import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.data.model.SortingOption
import io.reactivex.Single

interface MoviesRepository {

    fun getMovies(page: Int): Single<List<Movie>>

    fun setCurrentSortingOption(sortingOption: SortingOption)

}