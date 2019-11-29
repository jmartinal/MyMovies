package com.jmartinal.mymovies.model

import android.app.Activity
import android.location.Geocoder
import android.location.Location

class RegionRepository(activity: Activity) {

    private val geocoder = Geocoder(activity)
    private val locationDataSource = PlayServicesLocationDataSource(activity)

    suspend fun findLastRegion(): String = findLastLocation().toRegion()

    private suspend fun findLastLocation(): Location? {
        return locationDataSource.findLastLocation()
    }

    private fun Location?.toRegion(): String {
        val address = this?.let {
            geocoder.getFromLocation(latitude, longitude, 1)
        }
        return address?.firstOrNull()?.countryCode ?: DEFAULT_REGION
    }

    companion object {
        private const val DEFAULT_REGION = "US"
    }
}