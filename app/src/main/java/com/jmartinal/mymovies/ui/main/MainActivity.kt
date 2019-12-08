package com.jmartinal.mymovies.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.model.Movie
import com.jmartinal.mymovies.model.MoviesRepository
import com.jmartinal.mymovies.model.NetworkManager
import com.jmartinal.mymovies.model.PermissionsManager
import com.jmartinal.mymovies.ui.detail.MovieDetailActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), MainPresenter.View {

    private val permissionsManager by lazy { PermissionsManager(this) }

    private val presenter by lazy { MainPresenter(MoviesRepository(this), NetworkManager(this)) }
    private val adapter by lazy { MoviesAdapter(presenter::onMovieClicked) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (permissionsManager.checkPermissionsGranted()) {
            presenter.onCreate(this)
            moviesList.adapter = adapter
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsManager.permissionsNeeded, PERMISSION_REQUEST_CODE)
            }
        }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress() {
        moviesList.visibility = View.GONE
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        moviesList.visibility = View.VISIBLE
        progress.visibility = View.GONE
    }

    override fun updateData(movies: List<Movie>) {
        adapter.movies = movies
        adapter.notifyDataSetChanged()
    }

    override fun navigateTo(movie: Movie) {
        Intent(this@MainActivity, MovieDetailActivity::class.java).apply {
            putExtra(Constants.Communication.KEY_MOVIE, movie)
            startActivity(this)
        }
    }

    override fun showNoConnectivityError() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.error_title))
                .setMessage(getString(R.string.no_connectivity_error))
                .setPositiveButton(getString(R.string.ok)) { _, _ -> presenter.requestMovies() }
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
                presenter::requestMovies
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
