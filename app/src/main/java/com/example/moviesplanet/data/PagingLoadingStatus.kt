package com.example.moviesplanet.data

enum class Status {
    FIRST_LOADING,
    FIRST_LOADING_FAILED,
    FIRST_LOADING_SUCCESS,
    LOADING,
    LOADING_FAILED,
    LOADING_SUCCESS
}

data class PagingLoadingStatus private constructor(val status: Status, val message: String? = null) {
    companion object {
        val FIRST_LOADING = PagingLoadingStatus(Status.FIRST_LOADING)
        val FIRST_LOADING_SUCCESS = PagingLoadingStatus(Status.FIRST_LOADING_SUCCESS)
        val LOADING = PagingLoadingStatus(Status.LOADING)
        val LOADING_SUCCESS = PagingLoadingStatus(Status.LOADING_SUCCESS)
        fun firstLoadingError(message: String?) = PagingLoadingStatus(Status.FIRST_LOADING_FAILED, message)
        fun loadingError(message: String?) = PagingLoadingStatus(Status.LOADING_FAILED, message)
    }
}