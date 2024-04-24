package com.example.weatherforecastapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapp.data.database.models.PositionDb

@Dao
interface PositionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(position: PositionDb)

    @Query("SELECT * FROM position")
     fun getListPositions(): List<PositionDb>

    @Query("SELECT * FROM position WHERE position_id = :positionId")
    suspend fun getPosition(positionId: Int): PositionDb?

}