package com.example.moviesplanet.data.model

enum class Status {
    FIRST_LOADING,
    FIRST_LOADING_FAILED,
    FIRST_LOADING_SUCCESS,
    LOADING,
    LOADING_FAILED,
    LOADING_SUCCESS
}

data class LoadingStatus private constructor(val status: Status, val message: String? = null) {
    companion object {
        val FIRST_LOADING = LoadingStatus(Status.FIRST_LOADING)
        val FIRST_LOADING_SUCCESS = LoadingStatus(Status.FIRST_LOADING_SUCCESS)
        val LOADING = LoadingStatus(Status.LOADING)
        val LOADING_SUCCESS = LoadingStatus(Status.LOADING_SUCCESS)
        fun firstLoadingError(message: String?) = LoadingStatus(Status.FIRST_LOADING_FAILED, message)
        fun loadingError(message: String?) = LoadingStatus(Status.LOADING_FAILED, message)
    }
}