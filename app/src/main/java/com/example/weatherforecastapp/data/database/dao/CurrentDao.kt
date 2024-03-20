package com.example.weatherforecastapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.data.database.models.CurrentDb

@Dao
interface CurrentDao {

    //функция записи в баз двнных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insert(currentDb: CurrentDb)

    @Query("SELECT * FROM current_day WHERE id= :id")
     fun getCurrent(id: Int): LiveData<CurrentDb>


}