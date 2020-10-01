package com.example.data

import com.example.data.model.Movie
import com.example.data.storage.remote.model.*

val movie1 = Movie(
    id = 1,
    title = "title",
    releaseDate = "date",
    posterPath = "path",
    voteAverage = 10.0,
    overview = "overview",
    genres = listOf()
)

val movie2 = Movie(
    id = 2,
    title = "title2",
    releaseDate = "date2",
    posterPath = "path2",
    voteAverage = 10.0,
    overview = "overview2",
    genres = listOf()
)

val movies = listOf(movie1, movie2)

val reviewListResponse = ReviewListResponse(
    id = 1,
    results = listOf(ReviewResponse("1", "author", "content", "url"))
)

val videoListResponse = VideoListResponse(
    id = 1,
    results = listOf(VideoResponse("1", "", "", "key", "name", "site", 10, "type"))
)

val movieResponse1 =  MoviesResponse("", true, "", "", listOf(), 1, "", "", "", "", 0.0, 1, true, 0.0)

val movieResponse2 =  MoviesResponse("", true, "", "", listOf(), 2, "", "", "", "", 0.0, 1, true, 0.0)

val movieListResponse = MovieListResponse(
    page = 1,
    results = listOf(movieResponse1, movieResponse2),
    totalResults = 2,
    totalPages = 999
)

val genreListResponse = GenreListResponse(
    genres = listOf(GenreResponse(1, "name1"), GenreResponse(2, "name2"))
)