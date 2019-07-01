package com.example.moviesplanet.data

import com.example.moviesplanet.data.model.Movie
import com.example.moviesplanet.data.model.MovieExternalInfo
import com.example.moviesplanet.data.model.SortingOption
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface MoviesRepository {

    fun getMovies(page: Int): Single<List<Movie>>

    fun getMovieExternalInfo(movie: Movie): Single<List<MovieExternalInfo>>

    fun setCurrentSortingOption(sortingOption: SortingOption)

}