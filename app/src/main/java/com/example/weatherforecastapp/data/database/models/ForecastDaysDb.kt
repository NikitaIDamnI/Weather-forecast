package com.example.weatherforecastapp.data.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_day",
    foreignKeys = [
        ForeignKey(
            entity = LocationDb::class,
            parentColumns = ["position_id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class ForecastDaysDb(
    @PrimaryKey
    val id: Int ,
    val nameCity: String,
    val timeLocation: String,
    val json: String,
)
