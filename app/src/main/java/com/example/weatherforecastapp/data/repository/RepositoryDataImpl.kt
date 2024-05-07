package com.example.weatherforecastapp.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.testapi.network.ApiService
import com.example.weatherforecastapp.data.database.dao.CurrentDao
import com.example.weatherforecastapp.data.database.dao.ForecastDayDao
import com.example.weatherforecastapp.data.database.dao.LocationDao
import com.example.weatherforecastapp.data.database.dao.PositionDao
import com.example.weatherforecastapp.data.database.models.PositionDb
import com.example.weatherforecastapp.data.mapper.Mapper
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Current
import com.example.weatherforecastapp.domain.models.ForecastDayCity
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class RepositoryDataImpl @Inject constructor(
    private val context: Application,
    private val apiService: ApiService,
    private val mapper: Mapper,
    private val currentDao: CurrentDao,
    private val forecastDayDao: ForecastDayDao,
    private val locationDao: LocationDao,
    private val positionDao: PositionDao
) : RepositoryData {

    override suspend fun saveUserPosition(positionDb: PositionDb): Boolean {
        val datePosition = positionDao.getPosition(CURRENT_LOCATION_ID) ?: NO_POSITION
        try {
            if (datePosition.position != positionDb.position) {
                positionDao.insert(positionDb)
                writingAPItoDatabase(datePosition, positionDb, POSITION_ID_START)
            }
        } catch (_: Exception) {

        }
        Log.d("Repository_Log", "saveUserLocation ")
        return true
    }

    suspend fun updateUserPosition(position: PositionDb) {
        writingAPItoDatabase(
            datePositionDb = NO_POSITION,
            thisPositionDb = position,
            positionId = CURRENT_LOCATION_ID
        )
    }

    suspend fun getUserPosition(): PositionDb {
        return positionDao.getPosition(CURRENT_LOCATION_ID) ?: NO_POSITION
    }


    override suspend fun getCityFromSearch(searchCity: SearchCity): City {
        val cityDto = apiService.getCityDto(city = searchCity.name)
        return mapper.mapperCityDtoToEntityCity(cityDto, context)
    }

    override suspend fun addNewCity(city: City) {
        val positionsId = getPositionId()

        val positionCity = city.location.position
        val datePosition = locationDao.checkCity(positionCity) ?: NO_POSITION
        val time = System.currentTimeMillis()

        val thisPositionDb = PositionDb(positionsId, positionCity, formatTimeFromEpoch(time))

        Log.d("Repository_Log", "thisPosition $thisPositionDb , datePosition | $datePosition")

            writingAPItoDatabase(datePosition, thisPositionDb, positionsId)

    }

    private suspend fun getPositionId(): Int {
      //  val positionUser = positionDao.getPosition(CURRENT_LOCATION_ID)
        var sumPositions = locationDao.getSumPosition()
        if (sumPositions  == NOT_POSITIONS){
            return POSITION_ID_NEXT
        }else{
            val lastPositionId = locationDao.getLastPositionId()
            sumPositions = lastPositionId + POSITION_ID_NEXT
        }
        return sumPositions
    }

    override suspend fun weatherUpdate() {
        Log.d("RepositoryDataImpl_Log", "weatherUpdate: ")
        val dataListLocation = locationDao.getAllLocations()
        val time = System.currentTimeMillis()
        try {
            if (dataListLocation.isNotEmpty()) {
                for (location in dataListLocation) {
                    val thisPositionDb =
                        PositionDb(
                            location.id,
                            location.position,
                            formatTimeFromEpoch(time)
                        )
                    val datePositionDb = PositionDb(
                        location.id,
                        location.position,
                        timeFormat = location.last_updated
                    )
                    writingAPItoDatabase(datePositionDb, thisPositionDb, location.positionId)
                }
            }
        } catch (_: Exception) {
        }
    }


    override suspend fun getUserLocation(): Location {
        return mapper.mapperLocationDbToEntityLocation(locationDao.getUserLocation())
    }


    override suspend fun numberOfCities(): Int {
        return locationDao.getSumPosition()
    }


    override suspend fun searchCity(city: String): List<SearchCity> {
        val searchCity = apiService.searchCity(city = city)
        return searchCity.map { mapper.mapperSearchCityDtoToEntitySearchCity(it) }
    }

    override suspend fun deleteCity(positionId: Int) {
        locationDao.deleteLocation(positionId)
        updatePositionToDb(positionId)
    }

    override fun getSizePager(): LiveData<Int> {
        return MediatorLiveData<Int>().apply {
            addSource(locationDao.getSizePager()) {
                if (value != it) {
                    value = it
                }
            }
        }
    }

    override fun getForecastDaysCity(): LiveData<List<ForecastDayCity>> {
        return MediatorLiveData<List<ForecastDayCity>>().apply {
            addSource(forecastDayDao.getForecastDay()) { forecastDaysList ->
                if (forecastDaysList != null) {
                    value = forecastDaysList.map { mapper.mapperForecastCityDbToEntityForecastCityDays(it) }
                } else {
                    emptyList<ForecastDayCity>()
                }
            }
        }
    }

    override fun getLocations(): LiveData<List<Location>> {
        return MediatorLiveData<List<Location>>().apply {
            addSource(locationDao.getAllLocationsLiveData()) { locationList ->
                if (locationList != null) {
                    value = locationList.map { mapper.mapperLocationDbToEntityLocation(it) }
                } else {
                    emptyList<Location>()
                }
            }
        }
    }

    override fun getCurrentDays(): LiveData<List<Current>> {
        return MediatorLiveData<List<Current>>().apply {
            addSource(currentDao.getCurrents()) { currentList ->
                if (currentList != null) {
                    value = currentList.map { mapper.mapperCurrentDbToEntityCurrent(it, context) }
                } else {
                    emptyList<Current>()
                }
            }
        }
    }


    private suspend fun writingAPItoDatabase(
        datePositionDb: PositionDb,
        thisPositionDb: PositionDb,
        positionId: Int
    ) {
        if (checkingForUpdates(datePositionDb, thisPositionDb)) {
            val city = apiService.getCityDto(city = thisPositionDb.position)

            locationDao.insert(
                mapper.mapperCityDtoToLocationDb(
                    id = thisPositionDb.id,
                    cityDto = city,
                    position = "${city.locationDto.lat},${city.locationDto.lon}",
                    positionId = positionId,
                    timeUpdate = thisPositionDb.timeFormat
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

    private suspend fun updatePositionToDb(oldPositionId: Int) {
        val sumPosition = locationDao.getSumPosition()
        for (position in oldPositionId..sumPosition) {
            val positionElement = oldPositionId + 1
            locationDao.updatePosition(positionElement, position)
        }
    }


    private fun checkingForUpdates(
        datePositionDb: PositionDb,
        thisPositionDb: PositionDb
    ): Boolean {
        val thisHour = formatTimeByHour(thisPositionDb.timeFormat)
        val dataHour = formatTimeByHour(datePositionDb.timeFormat)
        Log.d(
            "RepositoryDataImpl",
            "checkingForUpdates: datePosition -  ${datePositionDb.position}," +
                    " thisPosition ${thisPositionDb.position} , ${datePositionDb.position != thisPositionDb.position} "
        )
        Log.d(
            "RepositoryDataImpl",
            "checkingForUpdates: thisHour -  $thisHour," +
                    " dataHour $dataHour , ${thisHour != dataHour}"
        )

        return (datePositionDb.position != thisPositionDb.position || thisHour != dataHour)
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
        const val NOT_POSITIONS = 0
        const val CURRENT_LOCATION_ID = 0
        const val POSITION_ID_START = 0
        const val POSITION_ID_NEXT = 1
       private val NO_POSITION = PositionDb(-1, "", "")
    }


}
