package com.example.weatherforecastapp.data.database.models

import androidx.room.ColumnInfo

data class Position(
    @ColumnInfo(name = "position_id") val id: Int,
    @ColumnInfo(name = "position") val position:String ,
    @ColumnInfo(name = "last_updated_epoch")  val timeEpoch : Long,
    @ColumnInfo(name ="last_updated") val timeFormat: String
)