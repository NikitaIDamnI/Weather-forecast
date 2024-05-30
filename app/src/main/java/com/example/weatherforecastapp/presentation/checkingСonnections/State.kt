package com.example.weatherforecastapp.presentation.checkingСonnections

data class State(
    val internet: Boolean = false,
    val location: Boolean = false,
    val locationPermission: Boolean = false,
    val firstWeatherUpdate: Boolean = true


)
