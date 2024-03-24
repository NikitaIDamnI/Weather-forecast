package com.example.weatherforecastapp.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.testapi.network.ApiFactory
import com.example.testapi.network.ApiService
import com.example.weatherforecastapp.data.database.AppDatabase
import com.example.weatherforecastapp.data.database.models.Position
import com.example.weatherforecastapp.data.mapper.Mapper
import com.example.weatherforecastapp.domain.models.Current
import com.example.weatherforecastapp.domain.models.ForecastDay
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepositoryDataImpl(
    context: Application
) : RepositoryData {
    private val apiService: ApiService = ApiFactory.apiService
    private val mapper = Mapper()
    private val currentDao = AppDatabase.getInstance(context).currentDao()
    private val forecastDayDao = AppDatabase.getInstance(context).forecastDayDao()
    private val locationDao = AppDatabase.getInstance(context).locationDao()

    var upDate: ((Boolean) -> Unit)? = null


    override suspend fun saveUserLocation(position: Position): Boolean {
        val datePosition = locationDao.checkPosition(CURRENT_LOCATION_ID) ?: NO_POSITION
        //Log.d("Repository_Log", "saveUserLocation($position) datePosition| $datePosition")
        //if(datePosition.position != position.position) {
        writingAPItoDatabase(datePosition, position, POSITION_ID_START)
        //addNewCity(searchCity("Костанай")[0])
        // addNewCity(searchCity("Курган")[0])
        //deleteCity(2)
        weatherUpdate()
        //writingAPItoDatabase(datePosition, position, POSITION_ID_START)
        // }
        Log.d("Repository_Log", "saveUserLocation ")
        return true
    }


    override suspend fun addNewCity(searchCity: SearchCity) {
        val positionId = locationDao.getSumPosition()
        val datePosition = locationDao.checkPosition(searchCity.id) ?: NO_POSITION
        val time = System.currentTimeMillis()
        val searchPosition = "${searchCity.lat},${searchCity.lon}"
        val thisPosition = Position(searchCity.id, searchPosition, time, formatTimeFromEpoch(time))
        Log.d("Repository_Log", "thisPosition $thisPosition ")


        writingAPItoDatabase(datePosition, thisPosition, positionId)
    }

    private suspend fun weatherUpdate() { // WORKER
        val dataListLocation = locationDao.getAllLocations()
        val time = System.currentTimeMillis()
        var positionId = POSITION_ID_START

        for (location in dataListLocation) {
            val thisPosition =
                Position(location.id, location.position, time, formatTimeFromEpoch(time))
            val datePosition = Position(
                location.id,
                location.position,
                location.last_updated_epoch,
                timeFormat = location.last_updated
            )

             Log.d("Repository_Log", "weatherUpdate thisPosition -$thisPosition ||" + "datePosition| $datePosition" + " positionId | $positionId")
            writingAPItoDatabase(datePosition, thisPosition, positionId)
            positionId += POSITION_ID_NEXT
        }
        upDate?.invoke(true)
    }


    override suspend fun numberOfCities(): Int {
        return locationDao.getSumPosition()
    }


    override suspend fun searchCity(city: String): List<SearchCity> {
        val searchCity = apiService.searchCity(city = city)
        return searchCity.map { mapper.mapperSearchCityDtoToEntitySearchCity(it) }
    }

    override suspend fun deleteCity(cityId: Int) {
        locationDao.deleteLocation(cityId)
    }

    override fun gerLocation(id: Int): LiveData<Location> {
        val locationLiveDataDb = locationDao.getLocation(id)
        return MediatorLiveData<Location>().apply {
            addSource(locationLiveDataDb) {
                if (it != null) {
                    value = mapper.mapperLocationDbToEntityLocation(it)
                }
            }
        }
    }

    override fun getCurrentDay(id: Int): LiveData<Current> {
        val currentLiveDataDb = currentDao.getCurrent(id)

        return MediatorLiveData<Current>().apply {
            addSource(currentLiveDataDb) {
                if (it != null) {
                    value = mapper.mapperCurrentDbToEntityCurrent(it)
                }
            }
        }
    }

    override fun forecastDas(id: Int): LiveData<List<ForecastDay>> {
        val forecastDayLiveDataDb = forecastDayDao.getForecastDay(id)

        return MediatorLiveData<List<ForecastDay>>().apply {
            addSource(forecastDayLiveDataDb) {
                if (it != null) {
                    value = mapper.mapperForecastDaysDbToEntityForecastDays(it)
                }
            }
        }
    }


    override suspend fun getLocations(): List<Location> {
        return locationDao.getAllLocations().map { mapper.mapperLocationDbToEntityLocation(it) }
    }


    private suspend fun writingAPItoDatabase(
        datePosition: Position,
        thisPosition: Position,
        positionId: Int
    ) {
        if (checkingForUpdates(datePosition, thisPosition)) {
            val city = apiService.forecastDays(city = thisPosition.position)
            // Log.d("Repository_Log", "writingAPItoDatabase|$city")

            locationDao.insert(
                mapper.mapperCityDtoToLocationDb(
                    id = thisPosition.id,
                    cityDto = city,
                    position = "${city.locationDto.lat},${city.locationDto.lon}",
                    positionId = positionId,
                    timeUpdate = thisPosition.timeFormat
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
        val updateTime = datePosition.timeEpoch + UPDATE_TIME
        val thisHour = formatTimeByHour(thisPosition.timeFormat)
        val dataHour = formatTimeByHour(datePosition.timeFormat)
        Log.d("Repository_Log", "checkingForUpdates thisHour - $thisHour" +
                "dataHour - $dataHour")

        return (datePosition.position != thisPosition.position || thisHour != dataHour)

    }


    private fun formatTimeFromEpoch(timeEpoch: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeEpoch
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun formatTimeByHour(time: String): String {
        return time.split(":")[0]

    }

    companion object {
        const val CURRENT_LOCATION_ID = 0
        const val UPDATE_TIME = 3600
        const val POSITION_ID_START = 0
        const val POSITION_ID_NEXT = 1

        val NO_POSITION = Position(-1, "", 0, "")
    }


}
