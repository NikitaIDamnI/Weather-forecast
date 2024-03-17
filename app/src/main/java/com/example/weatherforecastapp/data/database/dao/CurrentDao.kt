package com.example.weatherforecastapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weatherforecastapp.data.database.models.CurrentDb

@Dao
interface CurrentDao {

    //функция записи в баз двнных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currentDb: CurrentDb)

    @Update
    fun update(location: CurrentDb)

    @Query("DELETE FROM current_day WHERE id =:id")
    suspend fun deleteCurrent(id: Int)

    @Query("SELECT * FROM current_day WHERE id= :id")
    fun getCurrent(id:Int): CurrentDb




}