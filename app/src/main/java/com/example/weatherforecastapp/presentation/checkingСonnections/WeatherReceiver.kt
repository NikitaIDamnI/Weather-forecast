package com.example.weatherforecastapp.presentation.checkingСonnections

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class WeatherReceiver : BroadcastReceiver() {
    var checkLocationReceiver: ((Boolean) -> Unit)? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        when (action) {
            ACTION_LOCATION -> {
                // Проверяем доступность GPS
                val isGpsEnabled = intent.getBooleanExtra(LOCATION_CONDITION, true)
                checkLocationReceiver?.invoke(isGpsEnabled)

            }
        }
    }


    companion object {
        const val ACTION_LOCATION = "action_location"
        const val LOCATION_CONDITION = "condition"
    }

}