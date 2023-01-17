package com.abarghouthi.moviesapp.ui.details

import com.abarghouthi.moviesapp.data.models.MovieDetails

data class MovieDetailsState(
    val isLoading: Boolean = false,
    val movieDetails: MovieDetails? = null,
    val error: Exception? = null
)