package com.example.moviesplanet.data.storage.local.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieWithGenres(@Embedded val movie: MovieEntity,
                           @Relation(parentColumn = "movieId", entityColumn = "genreId", associateBy = Junction(MovieGenreCrossRef::class)) val genres: List<GenreEntity>)