package com.example.weatherforecastapp.domain.models

data class ForecastDay(
    val nameCity: String,
    val timeLocation: String,
    val date: String, // Дата прогноза
    val dateEpoch: Long, // Дата прогноза в формате Unix Epoch
    val days: Day, // Прогноз на день
    val astro: Astro, // Информация о солнце и луне
    val forecastHour: List<ForecastHour> // Прогноз на часы в течение дня
)
