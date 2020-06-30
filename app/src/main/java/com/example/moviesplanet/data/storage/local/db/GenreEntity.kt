package com.example.moviesplanet.data.storage.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GenreEntity(@PrimaryKey val genreId: Int,
                       val name: String)