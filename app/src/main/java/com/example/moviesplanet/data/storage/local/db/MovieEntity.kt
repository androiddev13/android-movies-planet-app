package com.example.moviesplanet.data.storage.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieEntity(@PrimaryKey val id: Int,
                       val name: String,
                       val releaseDate: String,
                       val posterPath: String,
                       val voteAverage: Double,
                       val overview: String)