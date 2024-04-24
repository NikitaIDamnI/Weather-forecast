package com.example.weatherforecastapp.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "position")
data class PositionDb(
    @PrimaryKey
    @ColumnInfo(name = "position_id") val id: Int,
    @ColumnInfo(name = "position") val position:String ,
    @ColumnInfo(name ="last_updated") val timeFormat: String
)