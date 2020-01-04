package com.jmartinal.mymovies.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.MovieApp
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.databinding.ActivityMainBinding
import com.jmartinal.mymovies.model.NetworkManager
import com.jmartinal.mymovies.model.PermissionsManager
import com.jmartinal.mymovies.model.database.Movie
import com.jmartinal.mymovies.model.server.MoviesRepository
import com.jmartinal.mymovies.ui.detail.MovieDetailActivity
import com.jmartinal.mymovies.ui.main.MainUIError.GenericError
import com.jmartinal.mymovies.ui.main.MainUIError.NetworkError
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val permissionsManager by lazy { PermissionsManager(application) }
    private val adapter by lazy { MoviesAdapter(viewModel::onMovieClicked) }
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(
            this,
            MainViewModel.MainViewModelFactory(
                MoviesRepository(applicationContext as MovieApp),
                NetworkManager(application)
            )
        )[MainViewModel::class.java]

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        showProgress()

        if (permissionsManager.checkPermissionsGranted()) {
            moviesList.adapter = adapter
            viewModel.error.observe(this, Observer(::showError))
            viewModel.moviesList.observe(this, Observer(::showMovies))
            viewModel.navigateToDetails.observe(this, Observer(::navigateTo))
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsManager.permissionsNeeded, PERMISSION_REQUEST_CODE)
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
        hideProgress()
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
        hideProgress()
        val message = when (error) {
            GenericError -> getString(R.string.no_connectivity_error)
            NetworkError -> getString(R.string.no_connectivity_error)
        }
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.error_title))
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    finish()
                }
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
                viewModel.error.observe(this@MainActivity, Observer(::showError))
                viewModel.moviesList.observe(this@MainActivity, Observer(::showMovies))
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
