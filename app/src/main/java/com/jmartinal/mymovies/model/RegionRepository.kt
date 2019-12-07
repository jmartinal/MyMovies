package com.jmartinal.mymovies.model

import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.jmartinal.mymovies.Constants
import java.io.IOException

class RegionRepository(context: Context) {

    private val locationDataSource = PlayServicesLocationDataSource(context)
    private val geocoder = Geocoder(context)

    suspend fun getCurrentRegion() = getCurrentLocation().toRegion()

    private suspend fun getCurrentLocation(): Location? {
        return locationDataSource.getLocation()
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