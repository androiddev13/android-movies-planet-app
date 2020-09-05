package com.example.moviesplanet.data.storage.remote.model

import com.google.gson.annotations.SerializedName

data class GenreListResponse(@SerializedName("genres") val genres: List<GenreResponse>?)