package com.example.weatherforecastapp.domain.models

data class ForecastDayCity(
    val nameCity: String = "",
    val timeLocation: String = "",
    val forecastDays: List<ForecastDay>
)
