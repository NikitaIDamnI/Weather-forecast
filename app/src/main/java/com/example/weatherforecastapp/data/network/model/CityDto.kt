package com.example.weatherforecastapp.data.network.model

import com.example.testapi.network.model.curentModels.CurrentDto
import com.example.testapi.network.model.curentModels.LocationDto
import com.example.testapi.network.model.forecastdaysModels.ForecastDaysDto
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("location") @Expose val locationDto: LocationDto,
    @SerializedName("current") @Expose val currentDto: CurrentDto,
    @SerializedName("forecast") @Expose val forecast : ForecastDaysDto
    )
