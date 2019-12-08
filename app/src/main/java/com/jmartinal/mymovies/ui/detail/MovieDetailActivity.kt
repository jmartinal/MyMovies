package com.jmartinal.mymovies.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.loadUrl
import com.jmartinal.mymovies.model.Movie
import kotlinx.android.synthetic.main.activity_movie_detail.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MovieDetailActivity : AppCompatActivity(), MovieDetailPresenter.View {

    private val presenter = MovieDetailPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movie: Movie = intent.extras?.get(Constants.Communication.KEY_MOVIE) as Movie
        presenter.onCreate(this, movie)

    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress() {
        content.visibility = View.GONE
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
        content.visibility = View.VISIBLE
    }

    override fun updateUI(movie: Movie) {
        movie.backdropPath?.let {
            movieBackdrop.loadUrl(Constants.TmdbApi.BACKDROP_BASE_URL + it)
        }
        moviePoster.loadUrl(Constants.TmdbApi.POSTER_BASE_URL + movie.posterPath)
        movieTitle.text = movie.title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            movieReleaseDate.text =
                LocalDate.parse(movie.releaseDate)
                    .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
        } else {
            val parser = SimpleDateFormat("yyyy-mm-dd")
            val date = parser.parse(movie.releaseDate)
            val formatter = SimpleDateFormat("MMMM dd, yyyy")
            val formattedDate = formatter.format(date)

            movieReleaseDate.text = formattedDate
        }
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
