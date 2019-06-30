package com.example.moviesplanet

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moviesplanet.data.MoviesRepository
import com.example.moviesplanet.data.SortingOption
import javax.inject.Inject

class MoviesViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel() {

    fun sortByPopularClick() {
        moviesRepository.setCurrentSortingOption(SortingOption.POPULAR)
        // TODO update current page
        moviesRepository.getMovies(0).subscribe({
            for (elem in it) {
                Log.d("AAAAA", elem.title)
            }
        }, {

        })
    }

    fun sortByTopRatedClick() {
        moviesRepository.setCurrentSortingOption(SortingOption.TOP_RATED)
        // TODO update current page
        moviesRepository.getMovies(0).subscribe({
            for (elem in it) {
                Log.d("AAAAA", elem.title)
            }
        }, {

        })
    }

}