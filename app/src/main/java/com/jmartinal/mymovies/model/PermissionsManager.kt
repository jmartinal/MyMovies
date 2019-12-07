package com.jmartinal.mymovies.model

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionsManager(val context: Context) {

    private val _permissionsNeeded: Array<String> = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val permissionsNeeded: Array<String> = _permissionsNeeded

    fun checkPermissionsGranted(): Boolean {
        for (permission in _permissionsNeeded) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}