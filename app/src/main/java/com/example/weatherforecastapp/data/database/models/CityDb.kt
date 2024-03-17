package com.example.weatherforecastapp.data.database.models

data class CityDb(
    val id: Int,
    val locationDb: LocationDb,
    val currentDb: CurrentDb,
    val forecastDaysDb: ForecastDaysDb,
)
