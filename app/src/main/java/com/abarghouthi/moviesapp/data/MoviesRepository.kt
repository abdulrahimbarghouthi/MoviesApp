package com.abarghouthi.moviesapp.data

import androidx.paging.PagingData
import com.abarghouthi.moviesapp.data.models.Movie
import com.abarghouthi.moviesapp.data.models.MovieDetailsDto
import com.abarghouthi.moviesapp.data.models.MovieDto
import com.abarghouthi.moviesapp.data.models.MoviesResponse
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getMoviesFeed(): Flow<PagingData<Movie>>

    suspend fun getMovieDetailsById(id: Int): Flow<Resource<MovieDetailsDto>>
}