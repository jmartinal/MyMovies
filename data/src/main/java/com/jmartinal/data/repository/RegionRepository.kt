package com.jmartinal.data.repository

import com.jmartinal.data.PermissionManager
import com.jmartinal.data.PermissionManager.Permission
import com.jmartinal.data.source.LocationDataSource

class RegionRepository(
    private val locationDataSource: LocationDataSource,
    private val permissionManager: PermissionManager
) {

    suspend fun getCurrentRegion(): String {
        return if (permissionManager.checkPermissions(arrayListOf(Permission.COARSE_LOCATION))) {
            locationDataSource.getCurrentRegion() ?: DEFAULT_REGION
        } else {
            DEFAULT_REGION
        }
    }

    companion object {
        const val DEFAULT_REGION = "US"
    }

}