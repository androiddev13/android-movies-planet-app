package com.example.moviesplanet.data.storage.local

import com.example.moviesplanet.data.model.SortingOption

class MoviesLocalDataSource constructor(private val moviesPreferences: MoviesPreferences) {

    fun setSortingOption(sortingOption: SortingOption) {
        moviesPreferences.setCurrentSortingOption(sortingOption)
    }

    fun getSortingOption(): SortingOption {
        return moviesPreferences.getCurrentSortingOption()
    }
}