package com.example.moviesplanet.data

import android.content.SharedPreferences

private const val KEY_CURRENT_SORTING_OPTION = "sorting_option"

class AppPreferences constructor(private val preferences: SharedPreferences) {

    fun setCurrentSortingOption(sortingOption: SortingOption) {
        preferences.edit().putString(KEY_CURRENT_SORTING_OPTION, sortingOption.name).apply()
    }

    fun getCurrentSortingOption(): SortingOption {
        val option = preferences.getString(KEY_CURRENT_SORTING_OPTION, SortingOption.POPULAR.name)
        return option?.let { SortingOption.valueOf(it) } ?:SortingOption.POPULAR
    }

}