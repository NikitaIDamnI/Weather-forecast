package com.example.weatherforecastapp.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_day")
data class ForecastDaysDb(
    @PrimaryKey
    val nameCity: String,
    val json: String,
)
