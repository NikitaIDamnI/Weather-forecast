package com.example.weatherforecastapp.domain.models

data class WeatherPrecipitation(
    val name: String,
    val value: Double,
    val maxValue: Int = MAX_VALUE,
    val normalValue: Int = NORMAL_VALUE,
    val minValue: Int = MIN_VALUE,
    val unit: String
) {
    companion object {
        const val MIN_VALUE = 0
        const val MAX_VALUE = 100
        const val NORMAL_VALUE = 0
        const val VALUE_KM_H = "km/h"
        const val VALUE_PERCENT = "percent"
        const val VALUE_MM = "mm"
        const val VALUE_DEGREE= "°"
        const val VALUE_MM_HG= "mm Hg"
        const val NOT_VALUE= ""


    }
}