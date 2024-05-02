package com.example.weatherforecastapp.presentation.checkingСonnections

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class WeatherReceiver(val context: Context) : BroadcastReceiver() {


    private var isFirstCheck = true
    private var isInternetAvailable = false

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val handler = Handler(Looper.getMainLooper())


    var checkLocationCondition: ((Boolean) -> Unit)? = null
    var checkInternetCondition: ((Boolean) -> Unit)? = null


    fun startReceiver() {
        val intentFilter = IntentFilter().apply {
            addAction(ACTION_LOCATION)
        }
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        context.registerReceiver(this, intentFilter, AppCompatActivity.RECEIVER_EXPORTED)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        when (action) {
            ACTION_LOCATION -> {
                // Проверяем доступность GPS
                val isGpsEnabled = intent.getBooleanExtra(LOCATION_CONDITION, true)
                checkLocationCondition?.invoke(isGpsEnabled)
            }
        }
    }



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


    private fun checkInternet(isConnected: Boolean) {
        if (isConnected != isInternetAvailable || isFirstCheck) {
            isFirstCheck = false
            isInternetAvailable = isConnected
            Log.d("InternetConnectionChecker", "checkInternet: $isInternetAvailable")
            handler.post {
                checkInternetCondition?.invoke(isInternetAvailable)

            }
        }
    }
    fun stopReceiver(){
        connectivityManager.unregisterNetworkCallback(networkCallback)
        context.unregisterReceiver(this)
    }


    companion object {
        const val ACTION_LOCATION = "action_location"
        const val LOCATION_CONDITION = "condition"
    }

}