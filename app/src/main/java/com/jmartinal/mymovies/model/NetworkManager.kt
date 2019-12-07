package com.jmartinal.mymovies.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkManager(context: Context) {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isConnected(): Boolean {
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