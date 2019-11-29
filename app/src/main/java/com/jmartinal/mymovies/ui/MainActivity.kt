package com.jmartinal.mymovies.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.model.MoviesRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val moviesRepository by lazy {
        MoviesRepository(
            this@MainActivity,
            resources.configuration.locales[0].language
        )
    }

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val adapter: MoviesAdapter = MoviesAdapter {
        Intent(this@MainActivity, MovieDetailActivity::class.java).apply {
            putExtra(Constants.Communication.KEY_MOVIE, it)
            startActivity(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moviesList.adapter = adapter

        if (checkPermissionsGranted(permissions)) {
            requestMovies()
        } else {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE)
        }
    }

    private fun checkPermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                requestMovies()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun requestMovies() {
        GlobalScope.launch(Dispatchers.Main) {
            adapter.movies = moviesRepository.getMovies().results
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
