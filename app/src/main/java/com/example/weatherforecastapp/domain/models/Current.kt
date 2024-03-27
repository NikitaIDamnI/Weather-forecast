package com.example.weatherforecastapp.domain.models

data class Current(
    val id: Int = 0,
    val nameCity: String = "",
    val date: String  = "", // Дата прогноза
    val last_updated_epoch: Long = 0L, // Время последнего обновления погоды в формате Unix Epoch
    val last_updated: String ="", // Дата и время последнего обновления погоды
    val temp_c: Double = 0.0, // Температура в градусах Цельсия
    val is_day: Int = 0, // Показывает, является ли сейчас день (1 - да, 0 - нет)
    val currentDay: Day = Day(),
    val weatherPrecipitation : List<WeatherPrecipitation> = emptyList(),
    val astro: Astro = Astro(),
    val condition: Condition = Condition(), // Объект, содержащий текстовое описание погодных условий, иконку и код состояния

)


