package com.example.weatherforecastapp.domain.models

data class ForecastDay(
    val nameCity: String = "",
    val timeLocation: String = "",
    val date: String = "", // Дата прогноза
    val dateEpoch: Long = 0L, // Дата прогноза в формате Unix Epoch
    val days: Day = Day(), // Прогноз на день
    val astro: Astro = Astro(), // Информация о солнце и луне
    val forecastHour: List<ForecastHour> = emptyList() // Прогноз на часы в течение дня
)
