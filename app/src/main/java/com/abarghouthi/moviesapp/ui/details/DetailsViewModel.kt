package com.abarghouthi.moviesapp.ui.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abarghouthi.moviesapp.data.MoviesRepository
import com.abarghouthi.moviesapp.data.Resource
import com.abarghouthi.moviesapp.data.models.toMovieDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val moviesRepository: MoviesRepository, ) : ViewModel() {

    private var _state by mutableStateOf(MovieDetailsState())

    val state: MovieDetailsState
    get() = _state

    fun getMovieDetailsById(id: Int) {
        viewModelScope.launch {
            moviesRepository.getMovieDetailsById(id).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _state = MovieDetailsState(isLoading = true)
                    is Resource.Success -> {
                        resource.data?.let {
                            val movieDetails = it.toMovieDetails()
                            _state = MovieDetailsState(isLoading = false, movieDetails = movieDetails)
                        }
                    }
                    is Resource.Error -> _state = _state.copy(isLoading = false, error = resource.exception)
                }
            }
        }
    }
}