package com.jmartinal.mymovies.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey(autoGenerate = true) val databaseId: Long,
    val posterPath: String?,
    val adult: Boolean,
    val overview: String,
    val releaseDate: String,
    val id: Long,
    val originalTitle: String,
    val originalLanguage: String,
    val title: String,
    val backdropPath: String?,
    val popularity: Float,
    val video: Boolean,
    val voteCount: Int,
    val voteAverage: Float,
    val favorite: Boolean
)