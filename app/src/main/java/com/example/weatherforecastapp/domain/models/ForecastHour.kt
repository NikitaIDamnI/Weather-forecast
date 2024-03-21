package com.example.weatherforecastapp.domain.models

data class ForecastHour(
    val time: String, // Время
    val temp_c: Double, // Температура в градусах Цельсия
    val is_day: Int, // Показывает, день или ночь (0 - ночь, 1 - день)
    val condition: Condition, // Условия погоды
    val feelslike_c: Double, // Ощущаемая температура в градусах Цельсия

)