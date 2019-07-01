package com.example.moviesplanet.data.model

data class Movie (val id: Int, val title: String, val releaseDate: String, val posterPath: String,
                  val voteAverage: Double, val overview: String) {

    companion object {

        fun getEmpty() = Movie(0, "", "", "", 0.0, "")

    }

}