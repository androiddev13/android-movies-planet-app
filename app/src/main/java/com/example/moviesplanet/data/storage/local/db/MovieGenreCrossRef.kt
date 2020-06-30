package com.example.moviesplanet.data.storage.local.db

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "genreId"])
data class MovieGenreCrossRef(val movieId: Int,
                              val genreId: Int)