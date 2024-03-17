package com.example.weatherforecastapp.domain.models

data class Day(
    val maxtempC: Double, // Максимальная температура в градусах Цельсия
    val mintempC: Double, // Минимальная температура в градусах Цельсия
    val avgtempC: Double, // Средняя температура в градусах Цельсия
    val maxwindKph: Double, // Максимальная скорость ветра в километрах в час
    val totalprecipMm: Double, // Общее количество осадков в миллиметрах
    val totalsnowCm: Double, // Общее количество снега в сантиметрах
    val avgvisKm: Double, // Средняя видимость в километрах
    val avghumidity: Int, // Средняя влажность воздуха в процентах
    val dailyWillItRain: Int, // Будет ли дождь (0 - нет, 1 - да)
    val dailyChanceOfRain: Int, // Вероятность дождя в процентах
    val dailyWillItSnow: Int, // Будет ли снег (0 - нет, 1 - да)
    val dailyChanceOfSnow: Int, // Вероятность снега в процентах
    val condition: Condition, // Условия погоды
)
