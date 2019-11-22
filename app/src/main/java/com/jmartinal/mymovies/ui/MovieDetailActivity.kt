package com.jmartinal.mymovies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.loadUrl
import com.jmartinal.mymovies.model.Movie
import kotlinx.android.synthetic.main.activity_movie_detail.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        intent.extras?.apply {
            movie = get(Constants.Communication.KEY_MOVIE) as Movie
        }
        movie.backdropPath?.let {
            movieBackdrop.loadUrl(Constants.TmdbApi.BACKDROP_BASE_URL + it)
        }
        moviePoster.loadUrl(Constants.TmdbApi.POSTER_BASE_URL + movie.posterPath)
        movieTitle.text = movie.title
        movieReleaseDate.text =
            LocalDate.parse(movie.releaseDate).format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
        movieRating.apply {
            stepSize = 0.1f
            rating = movie.voteAverage / 2
        }
        movieOverview.text = movie.overview
        movieOriginalTitle.text = buildSpannedString {
            bold { append(getString(R.string.original_title)) }
            append(movie.originalTitle)
        }
        movieOriginalLanguage.text = buildSpannedString {
            bold { append(getString(R.string.original_language)) }
            append(movie.originalLanguage)
        }
        moviePopularity.text = buildSpannedString {
            bold { append(getString(R.string.popularity)) }
            append(movie.popularity.toString())
        }
    }
}
