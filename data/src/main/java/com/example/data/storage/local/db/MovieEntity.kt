package com.example.data.storage.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Movie

@Entity
data class MovieEntity(@PrimaryKey val movieId: Int,
                       val name: String,
                       val releaseDate: String,
                       val posterPath: String,
                       val voteAverage: Double,
                       val overview: String) {
    companion object {
        fun from(movie: Movie) = MovieEntity(movie.id, movie.title, movie.releaseDate, movie.posterPath, movie.voteAverage, movie.overview)
    }
}