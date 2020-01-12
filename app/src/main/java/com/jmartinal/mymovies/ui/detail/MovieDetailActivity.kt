package com.jmartinal.mymovies.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.jmartinal.data.repository.LanguageRepository
import com.jmartinal.data.repository.MoviesRepository
import com.jmartinal.data.repository.RegionRepository
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.MovieApp
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.data.AndroidPermissionManager
import com.jmartinal.mymovies.data.DeviceLanguageDataSource
import com.jmartinal.mymovies.data.PlayServicesLocationDataSource
import com.jmartinal.mymovies.data.database.RoomDataSource
import com.jmartinal.mymovies.data.server.TheMovieDbDataSource
import com.jmartinal.mymovies.databinding.ActivityMovieDetailBinding
import com.jmartinal.mymovies.ui.detail.MovieDetailViewModel.MovieDetailViewModelFactory
import com.jmartinal.usecases.GetMovieById
import com.jmartinal.usecases.ToggleMovieFavorite

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movieId = with(intent.extras) {
            this?.getLong(Constants.Communication.KEY_MOVIE, -1)
        } ?: -1

        val app = application as MovieApp

        val moviesRepository = MoviesRepository(
            RoomDataSource(app.database),
            TheMovieDbDataSource(),
            RegionRepository(
                PlayServicesLocationDataSource(app),
                AndroidPermissionManager(app, this@MovieDetailActivity)
            ),
            LanguageRepository(DeviceLanguageDataSource(app))
        )

        viewModel = ViewModelProviders.of(
            this,
            MovieDetailViewModelFactory(
                movieId, GetMovieById(moviesRepository), ToggleMovieFavorite(moviesRepository)
            )
        )[MovieDetailViewModel::class.java]

        val binding: ActivityMovieDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

    }

}
