package com.example.testapi.network.model.curentModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    @SerializedName("location") @Expose val locationDto: LocationDto,
    @SerializedName("current") @Expose val currentDto: CurrentDto
)