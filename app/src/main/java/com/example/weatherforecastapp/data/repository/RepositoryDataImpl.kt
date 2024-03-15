package com.example.weatherforecastapp.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.testapi.network.ApiFactory
import com.example.testapi.network.ApiService
import com.example.weatherforecastapp.data.database.AppDatabase
import com.example.weatherforecastapp.data.database.models.Position
import com.example.weatherforecastapp.data.mapper.Mapper
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import kotlinx.coroutines.delay

class RepositoryDataImpl(
    context: Application
) : RepositoryData {
    private val apiService: ApiService = ApiFactory.apiService
    private val mapper = Mapper()
    private val currentDao = AppDatabase.getInstance(context).currentDao()
    private val forecastDayDao = AppDatabase.getInstance(context).forecastDayDao()
    private val locationDao = AppDatabase.getInstance(context).locationDao()


    override suspend fun saveUserLocation(thisPosition: Position) {

        val datePosition = locationDao.checkPosition(CURRENT_LOCATION_ID) ?: NO_POSITION
        Log.d("Repository_Log", "searchCity($thisPosition) datePosition| $datePosition")

        if (checkingForUpdates(datePosition, thisPosition)) {
            var loading = true
            while (loading) {
                try {
                    val city = apiService.forecastDays(city = thisPosition.position)
                    locationDao.insert(
                        mapper.mapperCityDtoToLocationDb(
                            id = CURRENT_LOCATION_ID,
                            cityDto = city,
                            position = thisPosition.position,
                        )
                    )
                    currentDao.insert(mapper.mapperCityDtoToCurrentDb(city))
                    val forecastItem = mapper.mapperCityDtoToForecastDaysDb(city)
                    Log.d("Repository_Log", "forecastItem|$forecastItem")
                    forecastDayDao.insert(forecastItem)
                    loading = false
                } catch (e: Exception) {
                    Log.d("Repository_Log", "searchCity(${thisPosition.position})")
                }
                delay(2000)
            }
            Log.d("Repository_Log", "saveUserLocation = finish")
        } else {
            Log.d("Repository_Log", "not_update")
        }
    }



    override suspend fun weatherUpdate(city: String) {
        Log.d("Repository_Log", "weatherUpdate($city)")
    }

    override suspend fun searchCity(city: String): List<String> {
        Log.d("Repository_Log", "searchCity($city)")
        return mutableListOf()

    }

    override suspend fun deleteCity(cityId: Int) {
        Log.d("Repository_Log", "deleteCity($cityId)")
    }

    override fun getCity(cityId: Int): LiveData<City> {
        TODO("Not yet implemented")
    }

    override suspend fun getLocation(): List<Location> {
        TODO("Not yet implemented")
    }

    private fun checkingForUpdates(datePosition: Position, thisPosition: Position): Boolean {
        val updateTime = datePosition.timeEpoch + UPDATE_TIME
        return (datePosition.position != thisPosition.position
                || updateTime < thisPosition.timeEpoch && updateTime.toInt() != UPDATE_TIME)
    }


    companion object {
        const val CURRENT_LOCATION_ID = 1
        const val UPDATE_TIME = 3600
        val NO_POSITION = Position("", 0)
    }
}