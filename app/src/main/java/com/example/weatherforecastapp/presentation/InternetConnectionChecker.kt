package com.example.weatherforecastapp.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class InternetConnectionChecker(
    private val context: Context
) {

    private val connectivityReceiver = ConnectivityReceiver()
    var update: (() -> Unit)? = null
    var onInternetAvailable: (() -> Unit)? = null
    var onInternetUnavailable: (() -> Unit)? = null

    val  firstCondition = isInternetAvailable()

    var firstUpdate = true

    fun startListening() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(connectivityReceiver, filter)
    }

    fun stopListening() {
        context.unregisterReceiver(connectivityReceiver)
    }

    inner class ConnectivityReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isInternetAvailable()) {
                if (firstUpdate) {
                    update?.invoke()
                    firstUpdate = false
                }
                onInternetAvailable?.invoke()
            } else{
                onInternetUnavailable?.invoke()
            }
        }
    }

     fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}