package com.example.weatherforecastapp.presentation.checkingСonnections

data class StateReceiver(
    val internet: Boolean = true ,
    val location: Boolean = true,
    val locationPermission: Boolean = false ,
)