package com.jmartinal.mymovies.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.MovieApp
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.databinding.ActivityMovieDetailBinding
import com.jmartinal.mymovies.data.server.MoviesRepository
import com.jmartinal.mymovies.ui.detail.MovieDetailViewModel.MovieDetailViewModelFactory

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movieId = with(intent.extras) {
            this?.getLong(Constants.Communication.KEY_MOVIE, -1)
        } ?: -1

        viewModel = ViewModelProviders.of(
            this,
            MovieDetailViewModelFactory(movieId, MoviesRepository(application as MovieApp))
        )[MovieDetailViewModel::class.java]

        val binding: ActivityMovieDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

    }

}
