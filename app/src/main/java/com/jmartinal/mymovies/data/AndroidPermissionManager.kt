package com.jmartinal.mymovies.data

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jmartinal.data.PermissionManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener

class AndroidPermissionManager(val application: Application, private val activity: AppCompatActivity) :
    PermissionManager {

    override suspend fun checkPermissions(permissions: List<PermissionManager.Permission>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    application,
                    permission.toAndroidId()
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun requestPermission(permission: String, continuation: (Boolean) -> Unit) {
        Dexter
            .withActivity(activity)
            .withPermission(permission)
            .withListener(object : BasePermissionListener() {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    continuation(true)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    continuation(false)
                }
            })
            .check()
    }

    private fun PermissionManager.Permission.toAndroidId() = when (this) {
        PermissionManager.Permission.COARSE_LOCATION -> Manifest.permission.ACCESS_COARSE_LOCATION
    }

}
