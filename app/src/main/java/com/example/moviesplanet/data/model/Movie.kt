package com.example.moviesplanet.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie (val id: Int,
                  val title: String,
                  val releaseDate: String,
                  val posterPath: String,
                  val voteAverage: Double,
                  val overview: String,
                  val genres: List<Int>) : Parcelable {

    companion object {
        fun getEmpty() = Movie(0, "", "", "", 0.0, "", listOf())
    }
}