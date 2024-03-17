package com.example.testapi.network.model.forecastdaysModels

import com.google.gson.annotations.SerializedName

data class ForecastDaysDto(
    @SerializedName("forecastday") val days: List<ForecastDayDto>
)
