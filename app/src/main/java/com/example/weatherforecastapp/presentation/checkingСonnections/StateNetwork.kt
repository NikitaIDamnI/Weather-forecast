package com.example.weatherforecastapp.presentation.checkingСonnections

data class StateNetwork(
    val internet: Boolean = true,
    val location: Boolean = true,
    val locationPermission: Boolean = false,
    val firstWeatherUpdate: Boolean = true,
    val migration: Boolean = false,
    val shortNotifications: Boolean = true
)
