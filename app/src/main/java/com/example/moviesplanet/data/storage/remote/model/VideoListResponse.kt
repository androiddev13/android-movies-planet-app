package com.example.moviesplanet.data.storage.remote.model

import com.google.gson.annotations.SerializedName

data class VideoListResponse(@SerializedName("id") val id: Int,
                             @SerializedName("results") val results: List<VideoResponse>? = null)
