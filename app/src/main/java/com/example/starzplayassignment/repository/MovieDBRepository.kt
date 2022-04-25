package com.example.starzplayassignment.repository

import com.example.restapi.SUCCESS
import com.example.restapi.interfaces.ApiInterface
import com.example.restapi.models.MultiSearchMovieDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieDBRepository {
    suspend fun getMultiSearchMovieDBData(query: String, page: Int): MultiSearchMovieDataResponse? {
        try {
            val movieData = withContext(Dispatchers.IO) {
                ApiInterface.create().getMultiSearchDataAsync(query = query, page = page).await()
            }
            if (movieData.isSuccessful && movieData.code() == SUCCESS) {
                movieData.body()?.let {
                    return it
                } ?: run {
                    return null
                }
            } else {
                return null
            }
        } catch (exception: Exception) {
            return null
        }
    }
}