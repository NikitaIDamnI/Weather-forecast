package com.example.weatherforecastapp.domain.models

data class City(
    val location: Location,
    val current: Current,
    val forecastDays: List<ForecastDay>
)
