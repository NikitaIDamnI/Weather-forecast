package com.example.weatherforecastapp.domain.models

data class Location(
    val name: String,
    val temp_c:Double,
    val localtime: String,
    val position: String,
    val day_maxtempC: Double, // Максимальная температура в градусах Цельсия
    val day_mintempC: Double, // Минимальная температура в градусах Цельсия
    val condition_text: String,
    val condition_icon: String,
    val condition_code: Int
)
