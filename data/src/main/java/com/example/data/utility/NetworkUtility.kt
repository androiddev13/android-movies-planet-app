package com.example.data.utility

import com.example.data.model.Result
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>,
                                  errorMessage: String): Result<T> {
    return try {
        call()
    } catch (e: Exception) {
        Timber.e(e)
        Result.Error(IOException(errorMessage, e))
    }
}

fun <T : Any> Response<T>.safeApiResponseHandling(errorMessage: String): Result<T> {
    if (this.isSuccessful) {
        val body = this.body()
        if (body != null) {
            return Result.Success(body)
        }
    }
    return Result.Error(IOException("$errorMessage ${this.code()} ${this.message()}"))
}