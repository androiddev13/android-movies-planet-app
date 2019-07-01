package com.example.moviesplanet.data.storage.remote

import com.google.gson.annotations.SerializedName

data class ReviewListResponse(@SerializedName("id") val id: Int,
                              @SerializedName("results") val results: List<ReviewResponse>? = null)
