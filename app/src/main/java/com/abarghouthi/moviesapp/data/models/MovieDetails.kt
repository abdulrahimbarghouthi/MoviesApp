package com.abarghouthi.moviesapp.data.models

data class MovieDetails(
    val title: String,
    val backdropPath: String?,
    val posterPath: String?,
    val releaseDate: String?,
    val overview: String?
)