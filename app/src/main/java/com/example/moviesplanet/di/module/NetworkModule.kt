package com.example.moviesplanet.di.module

import android.util.Log
import com.example.moviesplanet.BuildConfig
import com.example.moviesplanet.data.storage.remote.MoviesServiceApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor())
                .addInterceptor(getQueryApiKeyInterceptor())
                .build()
        } else {
            OkHttpClient.Builder()
                .addInterceptor(getQueryApiKeyInterceptor())
                .build()
        }
    }

    @Singleton
    @Provides
    fun provideMoviesServiceApi(client: OkHttpClient): MoviesServiceApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MOVIES_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MoviesServiceApi::class.java)
    }

    companion object {

        private const val TAG_NETWORKING_LOG = "Networking"
        private const val KEY_API_KEY = "api_key"

        fun getLoggingInterceptor(): HttpLoggingInterceptor {
            val logger = HttpLoggingInterceptor.Logger { message -> Log.d(TAG_NETWORKING_LOG, message) }
            return HttpLoggingInterceptor(logger)
                .apply { level = HttpLoggingInterceptor.Level.BODY }
        }

        /**
         * @return [Interceptor] which adds query with api key to each request.
         */
        fun getQueryApiKeyInterceptor(): Interceptor {
            return Interceptor {
                val url = it.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter(KEY_API_KEY, BuildConfig.MOVIES_API_KEY)
                    .build()
                it.proceed(it.request().newBuilder().url(url).build())
            }
        }
    }

}