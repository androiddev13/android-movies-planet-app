package com.example.moviesplanet.data.model

data class MovieDetails(val movie: Movie,
                        val isFavorite: Boolean,
                        val externalInfo: List<MovieExternalInfo>,
                        val movieGenres: List<MovieGenre>)