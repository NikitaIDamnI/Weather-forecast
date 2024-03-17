package com.example.weatherforecastapp.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationDb(
    @ColumnInfo(name = "position_id")
    @PrimaryKey()
    val positionId:Int,
    val id: Int ,
    val name: String,
    val last_updated_epoch: Long,
    val region: String,
    val country: String,
    val position: String,
    val tz_id: String,
    val temp_c: Double,
    val localtime: String,
    val day_maxtempC: Double, // Максимальная температура в градусах Цельсия
    val day_mintempC: Double, // Минимальная температура в градусах Цельсия
    val condition_text: String,
    val condition_icon: String,
    val condition_code: Int
) {
    companion object {
        const val START_ID = 0
    }
}