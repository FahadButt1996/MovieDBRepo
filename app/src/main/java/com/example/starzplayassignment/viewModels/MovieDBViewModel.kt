package com.example.starzplayassignment.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restapi.models.MultiSearchMovieDataResponse
import com.example.starzplayassignment.repository.MovieDBRepository

class MovieDBViewModel(private val repository: MovieDBRepository) : ViewModel() {

    suspend fun getMultiSearchData(query: String, page: Int): MultiSearchMovieDataResponse? {
        return repository.getMultiSearchMovieDBData(query, page)
    }

    fun convertListToHashMap(
        movieData: MultiSearchMovieDataResponse,
        hashMap: HashMap<String, ArrayList<MultiSearchMovieDataResponse.Result>>
    ) {
        movieData.results.forEach {
            val data = hashMap[it.mediaType]
            if (data == null) {
                hashMap[it.mediaType] = ArrayList()
                hashMap[it.mediaType]?.add(it)
            } else {
                data.add(it)
            }
        }
        sortDataAlphabetically(hashMap)
    }

    private fun sortDataAlphabetically(hashMap: HashMap<String, ArrayList<MultiSearchMovieDataResponse.Result>>) {
        hashMap.keys.forEach {
            hashMap[it]?.sortBy { s -> s.title }
        }
    }

    class UserViewModelFactory(
        private val repository: MovieDBRepository
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MovieDBViewModel(repository) as T
        }
    }

}