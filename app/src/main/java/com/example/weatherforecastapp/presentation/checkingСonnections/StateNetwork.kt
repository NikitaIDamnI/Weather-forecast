package com.example.weatherforecastapp.presentation.checkingСonnections

import com.example.weatherforecastapp.domain.models.Notification

data class StateNetwork(
    val internet: Boolean = true,
    val location: Boolean = true,
    val locationPermission: Boolean = false,
    val firstWeatherUpdate: Boolean = true,
    val migration: Boolean = false,
    val fullNotification: Boolean = false,
    val listLNotifications: List<Notification> = emptyList()
){
    companion object{
        const val NOTIFICATION_NOT_LOCATION = 0
        const val NOTIFICATION_NOT_LOCATION_PERMISSION = 1
    }
}
