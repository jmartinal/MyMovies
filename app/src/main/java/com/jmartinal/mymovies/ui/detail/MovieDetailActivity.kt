package com.jmartinal.mymovies.ui.detail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.MovieApp
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.model.database.Movie
import com.jmartinal.mymovies.model.server.MoviesRepository
import com.jmartinal.mymovies.ui.common.loadUrl
import com.jmartinal.mymovies.ui.detail.MovieDetailUIModel.Loading
import com.jmartinal.mymovies.ui.detail.MovieDetailViewModel.MovieDetailViewModelFactory
import kotlinx.android.synthetic.main.activity_movie_detail.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movieId = with(intent.extras) {
            this?.getLong(Constants.Communication.KEY_MOVIE, -1)
        } ?: -1
        viewModel = ViewModelProviders.of(
            this,
            MovieDetailViewModelFactory(movieId, MoviesRepository(application as MovieApp))
        )[MovieDetailViewModel::class.java]
        viewModel.state.observe(this, Observer(::updateUI))

        movieDetailFavorite.setOnClickListener { viewModel.onFavoriteClicked() }


    }

    private fun updateUI(state: MovieDetailUIModel) {
        if (state == Loading) {
            showProgress()
        } else {
            hideProgress()
        }
        when (state) {
            is MovieDetailUIModel.ShowingResult -> (showMovieInfo(state.movie))
        }
    }

    private fun showProgress() {
        content.visibility = View.GONE
        progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progress.visibility = View.GONE
        content.visibility = View.VISIBLE
    }

    @SuppressLint("SimpleDateFormat")
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun showMovieInfo(movie: Movie) {
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

        val drawable = if (movie.favorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
        movieDetailFavorite.setImageDrawable(getDrawable(drawable))
    }
}
