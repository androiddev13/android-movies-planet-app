package com.example.data.storage.remote.model

import com.example.data.model.MovieExternalInfo
import com.google.gson.annotations.SerializedName

data class ReviewResponse(@SerializedName("id") val id: String,
                          @SerializedName("author") val author: String,
                          @SerializedName("content") val content: String,
                          @SerializedName("url") val url: String) {

    fun toMovieExternalInfo() = MovieExternalInfo(content, url)

}
