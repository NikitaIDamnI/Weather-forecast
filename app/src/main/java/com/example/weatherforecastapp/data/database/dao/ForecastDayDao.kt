package com.example.weatherforecastapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.data.database.models.ForecastDaysDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDayDao {

    //функция записи в баз двнных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forecastDaysDb: ForecastDaysDb)

    @Query("SELECT * FROM forecast_day")
    fun getForecastDayLiveData(): LiveData<List<ForecastDaysDb>>

    @Query("SELECT * FROM forecast_day")
    suspend fun getForecastDay(): List<ForecastDaysDb>
    @Query("SELECT * FROM forecast_day")
    fun getForecastDayFlow(): Flow<List<ForecastDaysDb>>


}