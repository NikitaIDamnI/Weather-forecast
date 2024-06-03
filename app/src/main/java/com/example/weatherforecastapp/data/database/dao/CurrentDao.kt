package com.example.weatherforecastapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.data.database.models.CurrentDb
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentDao {

    //функция записи в баз двнных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentDb: CurrentDb)

    @Query("SELECT * FROM current_day ")
    fun getCurrentsLiveData(): LiveData<List<CurrentDb>>

    @Query("SELECT * FROM current_day ")
    suspend fun getCurrents(): List<CurrentDb>

    @Query("SELECT * FROM current_day ")
     fun getCurrentsFlow(): Flow<List<CurrentDb>>

}