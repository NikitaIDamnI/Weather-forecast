package com.example.weatherforecastapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.data.database.models.LocationDb
import com.example.weatherforecastapp.data.database.models.Position

@Dao
interface LocationDao {

    //функция записи в баз двнных

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationDb)


    @Query("SELECT * FROM location")
    suspend fun getAllLocations(): List<LocationDb>

    @Query("SELECT * FROM location WHERE position_id =:id")
    suspend fun getLocation(id: Int): LocationDb

    @Query("SELECT position_id,position,last_updated_epoch FROM location WHERE position_id = :id")
    suspend fun checkPosition(id: Int): Position?

    @Query("SELECT position_id,position,last_updated_epoch FROM location WHERE position_id = :id")
    suspend fun checkCity(id: Int): Position?

    @Query("DELETE FROM location WHERE position_id =:id")
    suspend fun deleteLocation(id: Int)

    @Query("SELECT COUNT(*) FROM location")
    suspend fun getSumPosition(): Int

    @Query("DELETE FROM location")
    fun deleteAll()


}