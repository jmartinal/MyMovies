package com.jmartinal.mymovies.model

import android.app.Application
import android.location.Geocoder
import android.location.Location
import com.jmartinal.mymovies.Constants
import java.io.IOException

class RegionRepository(application: Application) {

    private val locationDataSource = PlayServicesLocationDataSource(application)
    private val geocoder = Geocoder(application)

    suspend fun getCurrentRegion() = getCurrentLocation().toRegion()

    private suspend fun getCurrentLocation(): Location? {
        return locationDataSource.findLastLocation()
    }

    private fun Location?.toRegion(): String {
        return try {
            val address = this?.let {
                geocoder.getFromLocation(it.latitude, it.longitude, 1)
            }
            address?.firstOrNull()?.countryCode ?: Constants.DEFAULT_REGION
        } catch (exception: IOException) {
            exception.printStackTrace()
            Constants.DEFAULT_REGION
        }
    }
}