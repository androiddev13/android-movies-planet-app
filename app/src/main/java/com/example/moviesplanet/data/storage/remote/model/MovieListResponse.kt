package com.example.moviesplanet.data.storage.remote.model

import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MoviesResponse>?,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int
)