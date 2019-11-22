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
// TODO Add complex parameters
    val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String,
//    @SerializedName("belongs_to_collection") val belongsToCollection: Collection?,
    val budget: Int,
//    val genres: List<Genre>,
    val homepage: String?,
    val id: Long,
    @SerializedName("imdb_id") val imdb_id: String?,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path") val posterPath: String?,
//    @SerializedName("production_companies") val productionCompanies: List<Company>,
//    @SerializedName("production_countries") val productionCountries: List<Country>,
    @SerializedName("release_date") val releaseDate: String,
    val revenue: Long,
    val runtime: Int?,
//    @SerializedName("spoken_languages") val spokenLanguages: List<Language>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
): Parcelable