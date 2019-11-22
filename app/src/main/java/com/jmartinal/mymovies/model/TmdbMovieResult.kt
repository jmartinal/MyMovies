package com.jmartinal.mymovies.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class TmdbMovieResult (
    val page: Int,
    val results: List<Movie>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

@Parcelize
data class Movie(
    @SerializedName("poster_path") val posterPath: String?,
    val adult: Boolean,
    val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    val id: Long,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("original_language") val originalLanguage: String,
    val title: String,
    @SerializedName("backdrop_path") val backdropPath: String?,
    val popularity: Float,
    val video: Boolean,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("vote_average") val voteAverage: Float
): Parcelable