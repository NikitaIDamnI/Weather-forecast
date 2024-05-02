package com.example.weatherforecastapp.presentation.checkingСonnections

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Handler
import android.os.Looper
import android.util.Log
import javax.inject.Inject

class InternetConnectionChecker @Inject constructor(
    application: Application
) {

    var update: (() -> Unit)? = null
    var onInternetAvailable: (() -> Unit)? = null
    var onInternetUnavailable: (() -> Unit)? = null

     private var isFirstCheck = true
    var isInternetAvailable = false

    private val connectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val handler = Handler(Looper.getMainLooper())

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            checkInternet(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            checkInternet(false)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            checkInternet(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
        }
    }

    fun startListening() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun stopListening() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun checkInternet(isConnected: Boolean) {
        if (isConnected != isInternetAvailable || isFirstCheck) {
            isFirstCheck = false
            isInternetAvailable = isConnected
            Log.d("InternetConnectionChecker", "checkInternet: $isInternetAvailable")
            handler.post {
                if (isInternetAvailable) {
                    update?.invoke()
                    onInternetAvailable?.invoke()
                } else {
                    onInternetUnavailable?.invoke()
                }
            }
        }
    }
}