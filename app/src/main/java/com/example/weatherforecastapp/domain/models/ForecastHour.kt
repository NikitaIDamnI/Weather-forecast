package com.example.weatherforecastapp.domain.models

data class ForecastHour(
    val time: String, // Время
    val temp_c: Int = 0, // Температура в градусах Цельсия
    val is_day: Int = 0, // Показывает, день или ночь (0 - ночь, 1 - день)
    val condition: Condition , // Условия погоды
    val feelslike_c: Int = 0, // Ощущаемая температура в градусах Цельсия

)