package com.example.weatherforecastapp.domain.models

data class Day(
    val maxtempC: Double = 0.0, // Максимальная температура в градусах Цельсия
    val mintempC: Double = 0.0, // Минимальная температура в градусах Цельсия
    val avgtempC: Double = 0.0, // Средняя температура в градусах Цельсия
    val maxwindKph: Double = 0.0, // Максимальная скорость ветра в километрах в час
    val totalprecipMm: Double = 0.0, // Общее количество осадков в миллиметрах
    val totalsnowCm: Double = 0.0, // Общее количество снега в сантиметрах
    val avgvisKm: Double = 0.0, // Средняя видимость в километрах
    val avghumidity: Int = 0, // Средняя влажность воздуха в процентах
    val dailyWillItRain: Int = 0, // Будет ли дождь (0 - нет, 1 - да)
    val dailyChanceOfRain: Int = 0, // Вероятность дождя в процентах
    val dailyWillItSnow: Int = 0, // Будет ли снег (0 - нет, 1 - да)
    val dailyChanceOfSnow: Int = 0, // Вероятность снега в процентах
    val condition: Condition = Condition(), // Условия погоды
)
