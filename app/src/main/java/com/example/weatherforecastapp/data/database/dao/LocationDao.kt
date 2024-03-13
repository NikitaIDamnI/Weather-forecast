package com.example.weatherforecastapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.data.database.models.LocationDb

@Dao
interface LocationDao {

    //функция записи в баз двнных

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(location: LocationDb)

        @Delete
        fun delete(location: LocationDb)

        @Query("SELECT * FROM location")
        fun getAllLocations(): List<LocationDb>








}