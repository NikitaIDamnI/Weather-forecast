package com.example.testapi.network.model.forecastdaysModels

import com.google.gson.annotations.SerializedName

data class AstroDto(
    @SerializedName("sunrise") val sunrise: String, // Время восхода солнца
    @SerializedName("sunset") val sunset: String, // Время заката солнца
    @SerializedName("moonrise") val moonrise: String, // Время восхода луны
    @SerializedName("moonset") val moonset: String, // Время заката луны
    @SerializedName("moon_phase") val moonPhase: String, // Фаза луны
    @SerializedName("moon_illumination") val moonIllumination: Int, // Освещенность луны в процентах
    @SerializedName("is_moon_up") val isMoonUp: Int, // Показывает, восходит ли луна (0 - нет, 1 - да)
    @SerializedName("is_sun_up") val isSunUp: Int // Показывает, восходит ли солнце (0 - нет, 1 - да)
)

