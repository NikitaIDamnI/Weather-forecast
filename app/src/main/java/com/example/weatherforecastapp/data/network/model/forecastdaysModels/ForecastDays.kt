package com.example.testapi.network.model.forecastdaysModels

import com.google.gson.annotations.SerializedName

data class ForecastDays(
    @SerializedName("forecastday") val days: List<ForecastDayDto>
)
