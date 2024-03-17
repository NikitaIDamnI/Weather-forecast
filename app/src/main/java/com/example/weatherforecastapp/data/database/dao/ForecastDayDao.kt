package com.example.weatherforecastapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.data.database.models.ForecastDaysDb

@Dao
interface ForecastDayDao {

    //функция записи в баз двнных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(forecastDaysDb: ForecastDaysDb)

    @Query("DELETE FROM forecast_day WHERE id =:id")
    suspend fun deleteForecastDay(id: Int)

    @Query("SELECT * FROM forecast_day WHERE id = :id")
    fun getForecastDay(id:Int):  ForecastDaysDb


}