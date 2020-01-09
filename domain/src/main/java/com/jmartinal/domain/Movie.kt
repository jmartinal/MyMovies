package com.jmartinal.domain

data class Movie(
    val id: Long,
    val adult: Boolean,
    val backdropPath: String?,
    val favorite: Boolean,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Float,
    val posterPath: String?,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Float,
    val voteCount: Int
)