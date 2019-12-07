package com.jmartinal.mymovies.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.model.MoviesRepository
import com.jmartinal.mymovies.model.NetworkManager
import com.jmartinal.mymovies.model.PermissionsManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val networkManager: NetworkManager by lazy { NetworkManager(this) }
    private val permissionsManager: PermissionsManager by lazy { PermissionsManager(this) }
    private val moviesRepository: MoviesRepository by lazy { MoviesRepository(this) }

    private val adapter: MoviesAdapter = MoviesAdapter {
        Intent(this@MainActivity, MovieDetailActivity::class.java).apply {
            putExtra(Constants.Communication.KEY_MOVIE, it)
            startActivity(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (permissionsManager.checkPermissionsGranted()) {
            if (networkManager.isConnected()) {
                findMovies()
            } else {
                notifyNetworkError()
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsManager.permissionsNeeded, PERMISSION_REQUEST_CODE)
            }
        }
        moviesList.adapter = adapter
    }

    private fun findMovies() {
        GlobalScope.launch(Dispatchers.Main) {
            adapter.movies = moviesRepository.findPopularMovies().results
            adapter.notifyDataSetChanged()
        }
    }

    private fun notifyNetworkError() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.error_title))
                .setMessage(getString(R.string.internet_error))
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    if (networkManager.isConnected()) {
                        findMovies()
                    } else {
                        notifyNetworkError()
                    }
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
                findMovies()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
