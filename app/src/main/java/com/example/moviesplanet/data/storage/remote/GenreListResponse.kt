package com.example.moviesplanet.data.storage.remote

import com.google.gson.annotations.SerializedName

data class GenreListResponse(@SerializedName("genres") val genres: List<GenreResponse>?)