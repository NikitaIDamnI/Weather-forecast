package com.example.testapi.network.model.searchCityModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("name") @Expose val name: String,
    @SerializedName("region") @Expose val region: String,
    @SerializedName("country") @Expose val country: String,
    @SerializedName("lat") @Expose val lat: Double,
    @SerializedName("lon") @Expose val lon: Double,
    @SerializedName("url") @Expose val url: String
)