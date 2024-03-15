package com.example.weatherforecastapp.data.database.models

import androidx.room.ColumnInfo

data class Position(
    @ColumnInfo(name = "position") val position:String,
    @ColumnInfo(name = "last_updated_epoch")  val timeEpoch : Long
)