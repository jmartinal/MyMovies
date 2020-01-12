package com.jmartinal.mymovies.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class AndroidConnectivityManager(application: Application) :
    com.jmartinal.data.ConnectivityManager {

    private val connectivityManager: ConnectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun isConnected(): Boolean {
        return isWifiConnected() || isMobileConnected()
    }

    @Suppress("DEPRECATION")
    private fun isWifiConnected(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            connectivityManager.activeNetworkInfo?.let {
                it.type == ConnectivityManager.TYPE_WIFI && it.isConnectedOrConnecting
            } ?: false
        }
    }

    @Suppress("DEPRECATION")
    private fun isMobileConnected(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            connectivityManager.activeNetworkInfo?.let {
                it.type == ConnectivityManager.TYPE_MOBILE && it.isConnectedOrConnecting
            } ?: false
        }
    }
}
