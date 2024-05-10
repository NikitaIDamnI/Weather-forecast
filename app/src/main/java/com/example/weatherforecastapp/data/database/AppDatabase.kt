package com.example.weatherforecastapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecastapp.data.database.dao.CurrentDao
import com.example.weatherforecastapp.data.database.dao.ForecastDayDao
import com.example.weatherforecastapp.data.database.dao.LocationDao
import com.example.weatherforecastapp.data.database.dao.PositionDao
import com.example.weatherforecastapp.data.database.models.CurrentDb
import com.example.weatherforecastapp.data.database.models.ForecastDaysDb
import com.example.weatherforecastapp.data.database.models.LocationDb
import com.example.weatherforecastapp.data.database.models.PositionDb

@Database(
    entities = [
        LocationDb::class,
        CurrentDb::class,
        ForecastDaysDb::class,
        PositionDb::class
    ], version = 13, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {


    companion object {
        private var db: AppDatabase? = null
        private const val DB_NAME = "Cities_Weather.db"
        private val LOCK = Any()
        fun getInstance(context: Context): AppDatabase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                db = instance
                return instance
            }
        }
    }

    abstract fun locationDao(): LocationDao
    abstract fun forecastDayDao(): ForecastDayDao
    abstract fun currentDao(): CurrentDao
    abstract fun positionDao(): PositionDao

}