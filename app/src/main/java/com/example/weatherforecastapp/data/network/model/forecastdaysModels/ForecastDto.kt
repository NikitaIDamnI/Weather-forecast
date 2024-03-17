package com.example.testapi.network.model.forecastdaysModels

import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("forecast") val forecast : ForecastDaysDto
)
