package com.example.weatherforecastapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weatherforecastapp.data.database.models.CurrentDb
import com.example.weatherforecastapp.domain.models.City

@Dao
interface CurrentDao {

    //функция записи в баз двнных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currentDb: CurrentDb)

    @Update
    fun update(location: CurrentDb)

    @Delete
    fun delete(location: CurrentDb)

    @Query("SELECT * FROM current_day WHERE nameCity = :nameCity")
    fun getCurrentDaoByName(nameCity: City): CurrentDb




}