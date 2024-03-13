package com.example.weatherforecastapp.domain.models

data class City(
    val id: Int,
    val location: Location,
    val currentDto: Current,
    val forecastDay: List<ForecastDay>
)
