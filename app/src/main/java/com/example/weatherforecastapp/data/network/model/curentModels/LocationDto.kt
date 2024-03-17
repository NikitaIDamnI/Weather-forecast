package com.example.testapi.network.model.curentModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName("name") @Expose val name: String, // Название местоположения
    @SerializedName("region") @Expose val region: String, // Регион
    @SerializedName("country") @Expose val country: String, // Страна
    @SerializedName("lat") @Expose val lat: Double, // Широта
    @SerializedName("lon") @Expose val lon: Double, // Долгота
    @SerializedName("tz_id") @Expose val tz_id: String, // Идентификатор временной зоны
    @SerializedName("localtime_epoch") @Expose val localtime_epoch: Long, // Локальное время в формате Unix Epoch
    @SerializedName("localtime") @Expose val localtime: String // Локальное время
)