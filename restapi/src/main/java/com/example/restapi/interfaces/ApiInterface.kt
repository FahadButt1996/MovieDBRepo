package com.example.restapi.interfaces

import com.example.restapi.utilities.BASE_URLS
import com.example.restapi.models.MultiSearchMovieDataResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ApiInterface {
    // Api for getting the multi search data
    @GET("3/search/multi")
    fun getMultiSearchDataAsync(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = "3d0cda4466f269e793e9283f6ce0b75e",
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): Deferred<Response<MultiSearchMovieDataResponse>>

    companion object {
        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URLS)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(customOkHttpClient())
                .build()

            return retrofit.create(ApiInterface::class.java)

        }

        private fun customOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
        }
    }
}