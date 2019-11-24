package com.jmartinal.mymovies.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.model.TmdbFactory.tmdbService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.resume

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        if (checkPermissions(permissions)) {
            getMoviesByDeviceRegion()
        } else {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE)
        }
        moviesList.adapter = adapter
    }

    private fun getMoviesByDeviceRegion() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        GlobalScope.launch(Dispatchers.Main) {
            val location = getLocation()
            val language = resources.configuration.locales[0].language
            val movies =
                tmdbService.listMostPopularMoviesAsync(getRegionFromLocation(location), language)
                    .await()
            adapter.movies = movies.results
            adapter.notifyDataSetChanged()
        }
    }

    private suspend fun getLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result)
                }
        }

    private fun getRegionFromLocation(location: Location?): String {
        val geocoder = Geocoder(this@MainActivity)
        val address = location?.let {
            geocoder.getFromLocation(location.latitude, location.longitude, 1)
        }
        return address?.firstOrNull()?.countryCode ?: "US"
    }

    private fun checkPermissions(permissions: Array<String>): Boolean {
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
                getMoviesByDeviceRegion()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
        private val TAG = MainActivity::class.java.simpleName
    }
}
