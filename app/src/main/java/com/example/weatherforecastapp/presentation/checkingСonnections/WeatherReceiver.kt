package com.example.weatherforecastapp.presentation.checkingСonnections

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class WeatherReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        when (action) {
            ACTION_LOCATION -> {
                // Проверяем доступность GPS
                val isGpsEnabled = intent.getBooleanExtra(LOCATION_CONDITION,true)

                if (isGpsEnabled) {
                    // GPS включен
                    Log.d("WeatherReceiver", "GPS is enabled")
                } else {
                    // GPS выключен
                    Log.d("WeatherReceiver", "GPS is disabled")
                }
            }
        }
    }


    companion object {
        const val ACTION_LOCATION = "action_location"
        const val LOCATION_CONDITION = "condition"
    }

}