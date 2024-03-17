package com.example.weatherforecastapp.domain.models

data class Astro(
    val sunrise: String, // Время восхода солнца
    val sunset: String, // Время заката солнца
    val moonrise: String, // Время восхода луны
    val moonset: String, // Время заката луны
    val moonPhase: String, // Фаза луны
    val moonIllumination: Int, // Освещенность луны в процентах
    val isMoonUp: Int, // Показывает, восходит ли луна (0 - нет, 1 - да)
    val isSunUp: Int // Показывает, восходит ли солнце (0 - нет, 1 - да)
)
