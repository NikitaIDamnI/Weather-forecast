package com.example.weatherforecastapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weatherforecastapp.data.database.models.ForecastDaysDb

@Dao
interface ForecastDayDao {

    //функция записи в баз двнных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(forecastDaysDb: ForecastDaysDb)

    @Update
    fun update(forecastHourDb: ForecastDaysDb)

    @Delete
    fun delete(forecastHourDb:ForecastDaysDb)

    @Query("SELECT * FROM forecast_day WHERE nameCity = :nameCity")
    fun getForecastDay(nameCity:String):  ForecastDaysDb


}