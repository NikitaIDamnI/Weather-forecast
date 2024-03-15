package com.example.weatherforecastapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Delete
    suspend fun delete(location: LocationDb)

    @Query("SELECT * FROM location")
    suspend fun getAllLocations(): List<LocationDb>


    @Query("SELECT position,last_updated_epoch FROM location WHERE id = :id")
    suspend fun checkPosition(id: Int): Position?

    @Query("DELETE FROM location")
    fun deleteAll()

   @Query("DELETE FROM sqlite_sequence WHERE name='location'")
    fun checkDelete()


}