package com.example.testapi.network.model.curentModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CurrentDto(
    @SerializedName("last_updated_epoch") @Expose val last_updated_epoch: Long,
    @SerializedName("last_updated") @Expose val last_updated: String,
    @SerializedName("temp_c") @Expose val temp_c: Double,
    @SerializedName("temp_f") @Expose val temp_f: Double,
    @SerializedName("is_day") @Expose val is_day: Int,
    @SerializedName("condition") @Expose val conditionDto: ConditionDto,
    @SerializedName("wind_mph") @Expose val wind_mph: Double,
    @SerializedName("wind_kph") @Expose val wind_kph: Double,
    @SerializedName("wind_degree") @Expose val wind_degree: Int,
    @SerializedName("wind_dir") @Expose val wind_dir: String,
    @SerializedName("pressure_mb") @Expose val pressure_mb: Double,
    @SerializedName("pressure_in") @Expose val pressure_in: Double,
    @SerializedName("precip_mm") @Expose val precip_mm: Double,
    @SerializedName("precip_in") @Expose val precip_in: Double,
    @SerializedName("humidity") @Expose val humidity: Int,
    @SerializedName("cloud") @Expose val cloud: Int,
    @SerializedName("feelslike_c") @Expose val feelslike_c: Double,
    @SerializedName("feelslike_f") @Expose val feelslike_f: Double,
    @SerializedName("vis_km") @Expose val vis_km: Double,
    @SerializedName("vis_miles") @Expose val vis_miles: Double,
    @SerializedName("uv") @Expose val uv: Double,
    @SerializedName("gust_mph") @Expose val gust_mph: Double,
    @SerializedName("gust_kph") @Expose val gust_kph: Double
)