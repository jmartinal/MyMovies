package com.jmartinal.mymovies.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.MovieApp
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.model.NetworkManager
import com.jmartinal.mymovies.model.PermissionsManager
import com.jmartinal.mymovies.model.database.Movie
import com.jmartinal.mymovies.model.server.MoviesRepository
import com.jmartinal.mymovies.ui.detail.MovieDetailActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val permissionsManager by lazy { PermissionsManager(application) }
    private val adapter by lazy { MoviesAdapter(viewModel::onMovieClicked) }
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(
            this,
            MainViewModel.MainViewModelFactory(
                MoviesRepository(applicationContext as MovieApp),
                NetworkManager(application)
            )
        )[MainViewModel::class.java]

        if (permissionsManager.checkPermissionsGranted()) {
            moviesList.adapter = adapter
            viewModel.state.observe(this, Observer(::updateUI))
            viewModel.navigateToDetails.observe(this, Observer(::navigateTo))
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsManager.permissionsNeeded, PERMISSION_REQUEST_CODE)
            }
        }
    }

    private fun updateUI(state: MainUIModel) {
        if (state == MainUIModel.Loading) {
            showProgress()
        } else {
            hideProgress()
        }
        when (state) {
            is MainUIModel.ShowingResult -> {
                showMovies(state.movies)
            }
            is MainUIModel.Navigating -> {
                navigateTo(state.movie)
            }
            is MainUIModel.ShowingError -> {
                showError(state.error)
            }
        }
    }

    private fun showProgress() {
        moviesList.visibility = View.GONE
        progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        moviesList.visibility = View.VISIBLE
        progress.visibility = View.GONE
    }

    private fun showMovies(movies: List<Movie>) {
        adapter.movies = movies
        adapter.notifyDataSetChanged()
    }

    private fun navigateTo(movie: Movie) {
        Intent(this@MainActivity, MovieDetailActivity::class.java).apply {
            putExtra(Constants.Communication.KEY_MOVIE, movie.id)
            startActivity(this)
        }
    }

    private fun showError(error: MainUIError) {
        val message = when (error) {
            MainUIError.NetworkError -> {
                getString(R.string.no_connectivity_error)
            }
        }
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.error_title))
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    viewModel.state.observe(
                        this@MainActivity,
                        Observer(::updateUI)
                    )
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> finish() }
                .create()
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                viewModel.state.observe(this@MainActivity, Observer(::updateUI))
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
