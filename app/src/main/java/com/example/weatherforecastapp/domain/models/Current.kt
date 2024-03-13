package com.example.weatherforecastapp.domain.models

data class Current(
    val nameCity: String,
    val date: String, // Дата прогноза
    val last_updated_epoch: Long, // Время последнего обновления погоды в формате Unix Epoch
    val last_updated: String, // Дата и время последнего обновления погоды
    val temp_c: Double, // Температура в градусах Цельсия
    val is_day: Int, // Показывает, является ли сейчас день (1 - да, 0 - нет)
    val currentDay : Day,
    val astro: Astro,
    val condition: Condition, // Объект, содержащий текстовое описание погодных условий, иконку и код состояния

)


