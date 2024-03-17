package com.example.weatherforecastapp.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.testapi.network.ApiFactory
import com.example.testapi.network.ApiService
import com.example.weatherforecastapp.data.database.AppDatabase
import com.example.weatherforecastapp.data.database.models.CityDb
import com.example.weatherforecastapp.data.database.models.Position
import com.example.weatherforecastapp.data.mapper.Mapper
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity
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


    override suspend fun saveUserLocation(position: Position) {
        val datePosition = locationDao.checkPosition(CURRENT_LOCATION_ID) ?: NO_POSITION
        Log.d("Repository_Log", "saveUserLocation($position) datePosition| $datePosition")
        //if(datePosition.position != position.position) {
        writingToDatabase(datePosition, position, CURRENT_LOCATION_ID)
        // }
    }


    override suspend fun addNewCity(searchCity: SearchCity) {
        val datePosition = locationDao.checkCity(searchCity.id) ?: NO_POSITION
        val time = System.currentTimeMillis() / 1000
        val thisPosition = Position(searchCity.id, searchCity.name, time)
        writingToDatabase(datePosition, thisPosition, searchCity.id)
    }

    private suspend fun weatherUpdate() { // WORKER
        val dataListLocation = locationDao.getAllLocations()
        val time = System.currentTimeMillis() / 1000
        for (location in dataListLocation) {
            val thisPosition = Position(location.id, location.name, time)
            val datePosition = Position(location.id, location.name, location.last_updated_epoch)
            writingToDatabase(datePosition, thisPosition, location.id)
        }
    }

    override suspend fun searchCity(city: String): List<SearchCity> {
        val searchCity = apiService.searchCity(city = city)
        return searchCity.map { mapper.mapperSearchCityDtoToEntitySearchCity(it) }
    }

    override suspend fun deleteCity(cityId: Int) {
        locationDao.deleteLocation(cityId)
    }

    override suspend fun getCity(cityId: Int): LiveData<City> {

        val cityDb = CityDb(
            id = cityId,
            locationDb = locationDao.getLocation(cityId),
            currentDb = currentDao.getCurrent(cityId),
            forecastDaysDb = forecastDayDao.getForecastDay(cityId)
        )

        return MutableLiveData<City>().map {
            mapper.mapperCityDbToEntityCity(cityDb)
        }
    }

    override suspend fun getLocation(): List<Location> {
        TODO("Not yet implemented")
    }


    private suspend fun writingToDatabase(
        datePosition: Position,
        thisPosition: Position,
        id: Int,
    ) {
        if (checkingForUpdates(datePosition, thisPosition)) {
            var loading = true
            while (loading) {
                try {
                    val city = apiService.forecastDays(city = thisPosition.position)
                    locationDao.insert(
                        mapper.mapperCityDtoToLocationDb(
                            id = id,
                            cityDto = city,
                            position = thisPosition.position,
                        )
                    )
                    currentDao.insert(
                        mapper.mapperCityDtoToCurrentDb(
                            id = id, cityDto = city
                        )
                    )
                    val forecastItem = mapper.mapperCityDtoToForecastDaysDb(
                        id = id, cityDto = city
                    )
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


    private fun checkingForUpdates(
        datePosition: Position,
        thisPosition: Position
    ): Boolean {

        val updateTime = datePosition.timeEpoch.let { +UPDATE_TIME }
        return (datePosition.position != thisPosition.position
                || updateTime < thisPosition.timeEpoch && updateTime.toInt() != UPDATE_TIME)
    }


    companion object {
        const val CURRENT_LOCATION_ID = 0
        const val UPDATE_TIME = 3600

        val NO_POSITION = Position(-1, "", 0)
    }
}