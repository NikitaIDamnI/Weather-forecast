package com.example.weatherforecastapp.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings


class WeatherReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        when (action) {
            Settings.ACTION_LOCATION_SOURCE_SETTINGS -> {

            }


        }
    }
}