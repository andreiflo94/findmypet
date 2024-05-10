package com.heixss.findmypet.presenter.searchpets

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.heixss.findmypet.domain.searchpets.PetRepository
import com.heixss.findmypet.domain.searchpets.Pet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PetsViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {


    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery

    private val _searchedPets = MutableStateFlow<PagingData<Pet>>(PagingData.empty())
    val searchedPets: Flow<PagingData<Pet>> = _searchedPets

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchPets(query: String) {
        viewModelScope.launch {
            try {
                petRepository.getPetPager(query = query)
                    .flow
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _searchedPets.value = pagingData
                    }
            } catch (e: Exception) {
                // Handle specific types of exceptions
                when (e) {
                    is IOException -> {
                        // Handle network IO exception
                        // Show a message to the user indicating network error
                        // e.g., _errorState.value = "Network error occurred. Please check your internet connection."
                    }
                    is HttpException -> {
                        // Handle HTTP exception (e.g., 404, 500)
                        // Show an appropriate message to the user or handle differently based on status code
                    }
                    else -> {
                        // Handle other types of exceptions
                        // Show a generic error message or log the error for debugging
                        // e.g., _errorState.value = "An unexpected error occurred. Please try again later."
                    }
                }
                // Optionally, you can also log the error for debugging purposes
                Log.e(PetsViewModel::class.java.simpleName, "Error occurred during searchPets: ${e.message}", e)
            }
        }
    }
}