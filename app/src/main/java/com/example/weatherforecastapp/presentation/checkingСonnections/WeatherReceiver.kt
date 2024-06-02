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
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather

class WeatherReceiver(val context: Context, private val viewModelWeather: ViewModelWeather) :
    BroadcastReceiver() {
    private var stateReceiver = StateReceiver()

    private var isFirstCheck = true
    private var isInternetAvailable = false

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val handler = Handler(Looper.getMainLooper())

    var getState: ((StateReceiver) -> Unit)? = null

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


    fun startReceiver() {
        Log.d("InternetConnectionChecker", "checkInternet: $isInternetAvailable")

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        val intentFilter = IntentFilter().apply {
            addAction(ACTION_LOCATION)
            addAction(ACTION_LOCATION_PERMISSION)
            addAction(ACTION_STATE)
        }
        val newState = stateReceiver.copy(internet = isInternetAvailable)
        stateReceiver = newState
        viewModelWeather.updateState(stateReceiver)

        context.registerReceiver(this, intentFilter, AppCompatActivity.RECEIVER_EXPORTED)
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent?.action
        when (action) {
            ACTION_LOCATION_PERMISSION -> {
                val isLocationPermission = intent.getBooleanExtra(CONDITION, false)
                val newState = stateReceiver.copy(locationPermission = isLocationPermission)
                stateReceiver = newState
                viewModelWeather.updateState(stateReceiver)
            }

            ACTION_LOCATION -> {
                val isLocation = intent.getBooleanExtra(CONDITION, stateReceiver.location)
                val newState = stateReceiver.copy(location = isLocation)
                stateReceiver = newState
                viewModelWeather.updateState(stateReceiver)
            }

            else -> {}
        }
    }



    private fun checkInternet(isConnected: Boolean) {
        if (isConnected != isInternetAvailable || isFirstCheck) {
            isFirstCheck = false
            isInternetAvailable = isConnected
            handler.post {
                val newState = stateReceiver.copy(internet = isInternetAvailable)
                stateReceiver = newState
                viewModelWeather.updateState(stateReceiver)
                Log.d("InternetConnectionChecker", "checkInternet: $isInternetAvailable")

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

        const val TAG = "WeatherReceiver_Log"
    }

}