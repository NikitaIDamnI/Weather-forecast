package com.example.weatherforecastapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.data.database.models.LocationDb
import com.example.weatherforecastapp.data.database.models.PositionDb

@Dao
interface LocationDao {

    //функция записи в баз двнных

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationDb)


    @Query("SELECT * FROM location")
    fun getAllLocationsLiveData(): LiveData<List<LocationDb>>

    @Query("SELECT * FROM location")
    suspend fun getAllLocations(): List<LocationDb>

    @Query("SELECT * FROM location WHERE position_id =:id")
    fun getLocation(id: Int): LiveData<LocationDb>

    @Query("SELECT * FROM location WHERE position_id = 0")
    fun getUserLocation(): LocationDb

    @Query("SELECT position_id,position,last_updated_epoch,last_updated FROM location WHERE position_id = :id")
    suspend fun checkPosition(id: Int): PositionDb?

    @Query("SELECT position_id,position,last_updated_epoch,last_updated FROM location WHERE  position= :position")
    suspend fun checkCity(position: String): PositionDb?

    @Query("DELETE FROM location WHERE position_id =:id")
    suspend fun deleteLocation(id: Int)

    @Query("SELECT COUNT(*) FROM location")
    suspend fun getSumPosition(): Int


    @Query("SELECT position_id FROM location ORDER BY position_id DESC LIMIT 1")
    suspend fun getLastPositionId(): Int


    @Query("SELECT COUNT(*) FROM location")
    fun getSizePager(): LiveData<Int>

    @Query("UPDATE location SET position_id = :newPositionId WHERE position_id =:oldPositionId")
    suspend fun updatePosition(oldPositionId: Int, newPositionId: Int)

    @Query("DELETE FROM location")
    fun deleteAll()


}