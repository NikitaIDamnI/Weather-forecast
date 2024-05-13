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
    private var state = State()

    private var isFirstCheck = true
    private var isInternetAvailable = false

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val handler = Handler(Looper.getMainLooper())

    var getState: ((State) -> Unit)? = null

    var checkLocationStatus: ((Boolean) -> Unit)? = null
    var checkInternetStatus: ((Boolean) -> Unit)? = null
    var checkLocationPermissionStatus: ((Boolean) -> Unit)? = null


    fun startReceiver() {
        val intentFilter = IntentFilter().apply {
            addAction(ACTION_LOCATION)
            addAction(ACTION_LOCATION_PERMISSION)
            addAction(ACTION_STATE)

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
            ACTION_STATE -> {
                val isGpsEnabled = intent.getBooleanExtra(LOCATION, true)
                val isLocationPermission = intent.getBooleanExtra(LOCATION_PERMISSION, true)
                 state = State(
                    location = isGpsEnabled,
                    locationPermission = isLocationPermission,
                    internet = isInternetAvailable
                )
                getState?.invoke(state)
            }

            ACTION_LOCATION -> {
                val isGpsEnabled = intent.getBooleanExtra(CONDITION, true)
                checkLocationStatus?.invoke(isGpsEnabled)
            }

            ACTION_LOCATION_PERMISSION -> {
                val isLocationPermission = intent.getBooleanExtra(CONDITION, true)
                checkLocationPermissionStatus?.invoke(isLocationPermission)
            }


            else -> {

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
                checkInternetStatus?.invoke(isInternetAvailable)
                val newState = state.copy(internet = isConnected)
                getState?.invoke(newState)

            }
        }
    }

    fun stopReceiver() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
        context.unregisterReceiver(this)
    }


    companion object {
        const val ACTION_STATE = "action_location"
        const val ACTION_LOCATION = "action_location"
        const val ACTION_LOCATION_PERMISSION = "action_location_permission"
        const val CONDITION = "condition"
        const val INTERNET = "internet_value"
        const val LOCATION = "location_value"
        const val LOCATION_PERMISSION = "location_permission_value"


    }

}