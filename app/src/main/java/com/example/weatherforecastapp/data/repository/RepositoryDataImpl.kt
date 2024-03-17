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
         writingAPItoDatabase(datePosition, position, POSITION_ID_START)
        // addNewCity(searchCity("Костанай")[0])
        // addNewCity(searchCity("Курган")[0])
        weatherUpdate()
        // writingAPItoDatabase(datePosition, position, POSITION_ID_START)
        // }
    }


    override suspend fun addNewCity(searchCity: SearchCity) {
        val positionId = locationDao.getSumPosition()
        val datePosition = locationDao.checkPosition(searchCity.id) ?: NO_POSITION
        val time = System.currentTimeMillis() / 1000
        val searchPosition = "${searchCity.lat},${searchCity.lon}"
        val thisPosition = Position(searchCity.id, searchPosition, time)
        writingAPItoDatabase(datePosition, thisPosition, positionId)
    }

    private suspend fun weatherUpdate() { // WORKER
        val dataListLocation = locationDao.getAllLocations()
        val time = System.currentTimeMillis() / 1000
        var positionId = POSITION_ID_START

        for (location in dataListLocation) {
            Log.d("Repository_Log", "weatherUpdate - location| $location;")
            val thisPosition = Position(location.id, location.position, time)
            val datePosition = Position(location.id, location.position, location.last_updated_epoch)

            Log.d(
                "Repository_Log",
                "weatherUpdate thisPosition -$thisPosition ||" +
                        "datePosition| $datePosition" + " positionId | $positionId"
            )
            writingAPItoDatabase(datePosition, thisPosition, positionId)
            positionId += POSITION_ID_NEXT



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


    private suspend fun writingAPItoDatabase(
        datePosition: Position,
        thisPosition: Position,
        positionId: Int
    ) {
        if (checkingForUpdates(datePosition, thisPosition)) {
            val city = apiService.forecastDays(city = thisPosition.position)
            Log.d("Repository_Log", "writingAPItoDatabase|$city")

            locationDao.insert(
                mapper.mapperCityDtoToLocationDb(
                    id = thisPosition.id,
                    cityDto = city,
                    position = "${city.locationDto.lat},${city.locationDto.lon}",
                    positionId = positionId
                )
            )
            currentDao.insert(
                mapper.mapperCityDtoToCurrentDb(
                    positionId = positionId, cityDto = city
                )
            )
            val forecastItem = mapper.mapperCityDtoToForecastDaysDb(
                positionId = positionId, cityDto = city
            )
            Log.d("Repository_Log", "forecastItem|$forecastItem")
            forecastDayDao.insert(forecastItem)

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
        const val POSITION_ID_START = 0
        const val POSITION_ID_NEXT = 1

        val NO_POSITION = Position(-1, "", 0)
    }
}