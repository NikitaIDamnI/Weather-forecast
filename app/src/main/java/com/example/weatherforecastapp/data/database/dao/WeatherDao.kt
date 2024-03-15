package com.example.weatherforecastapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface  WeatherDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert()

}
