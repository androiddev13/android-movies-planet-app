package com.example.data.storage.remote.model

import com.google.gson.annotations.SerializedName

data class GenreListResponse(@SerializedName("genres") val genres: List<GenreResponse>?) {

    companion object {
        fun getEmpty() = GenreListResponse(listOf())
    }
}