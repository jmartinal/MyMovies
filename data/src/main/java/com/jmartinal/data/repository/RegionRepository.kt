package com.jmartinal.data.repository

import com.jmartinal.data.source.LocationDataSource

class RegionRepository(private val locationDataSource: LocationDataSource) {

    suspend fun getCurrentRegion(): String {
        // TODO Check COARSE_LOCATION permission
        return locationDataSource.getCurrentRegion() ?: DEFAULT_REGION
    }

    companion object {
        const val DEFAULT_REGION = "US"
    }

}