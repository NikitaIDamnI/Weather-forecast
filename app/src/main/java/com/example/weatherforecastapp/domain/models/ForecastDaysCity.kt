package com.example.weatherforecastapp.domain.models

data class ForecastDaysCity(
    val nameCity: String = "",
    val timeLocation: String = "",
    val forecastDays: List<ForecastDay>
)
