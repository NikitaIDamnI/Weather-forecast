package com.example.weatherforecastapp.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationDb(
    @ColumnInfo(name = "position_id")
    @PrimaryKey()
    val positionId:Int = 0,
    val id: Int = 0 ,
    val name: String = "",
    val last_updated_epoch: Long = 0L,
    val last_updated: String = "",
    val region: String ="",
    val country: String ="",
    val position: String ,
    val tz_id: String ="",
    val temp_c: Double = 0.0,
    val localtime: String ="",
    val day_maxtempC: Double = 0.0, // Максимальная температура в градусах Цельсия
    val day_mintempC: Double = 0.0, // Минимальная температура в градусах Цельсия
    val condition_text: String ="",
    val condition_icon: String ="",
    val condition_code: Int = 0
)