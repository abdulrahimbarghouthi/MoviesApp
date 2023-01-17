package com.abarghouthi.moviesapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.abarghouthi.moviesapp.data.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(moviesRepository: MoviesRepository) : ViewModel() {

    val moviesPagingData = moviesRepository.getMoviesFeed().cachedIn(viewModelScope)
}