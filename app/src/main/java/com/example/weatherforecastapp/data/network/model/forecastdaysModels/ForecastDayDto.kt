package com.example.testapi.network.model.forecastdaysModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForecastDayDto(
    @SerializedName("date") val date: String, // Дата прогноза
    @SerializedName("date_epoch") val dateEpoch: Long, // Дата прогноза в формате Unix Epoch
    @SerializedName("day") val day: DayDto, // Прогноз на день
    @SerializedName("astro") val astro: AstroDto, // Информация о солнце и луне
    @SerializedName("hour") val hour: List<HourDto> // Прогноз на часы в течение дня
)
