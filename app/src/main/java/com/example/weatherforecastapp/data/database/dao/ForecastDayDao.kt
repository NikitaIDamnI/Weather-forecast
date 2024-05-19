package com.example.weatherforecastapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.data.database.models.ForecastDaysDb

@Dao
interface ForecastDayDao {

    //функция записи в баз двнных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forecastDaysDb: ForecastDaysDb)

    @Query("SELECT * FROM forecast_day")
    fun getForecastDay(): LiveData<List<ForecastDaysDb>>


}