package com.example.weatherforecastapp.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.testapi.network.ApiService
import com.example.weatherforecastapp.data.Format
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
        val datePosition = positionDao.getPosition(USER_ID) ?: NO_POSITION
        try {
            if (datePosition.position != positionDb.position) {
                Log.d("Repository_Log", "saveUserLocation ")

                positionDao.insert(positionDb)
                writingAPItoDatabase(datePosition, positionDb)
            }
        } catch (_: Exception) {

        }
        Log.d("Repository_Log", "saveUserLocation ")
        return true
    }

    suspend fun updateUserPosition() {
        val dataPosition = getUserPosition()
        Log.d("RepositoryDataImpl_Log", "updateUserPosition: $dataPosition")
        if (dataPosition != null) {
            val time = System.currentTimeMillis()
            writingAPItoDatabase(
                datePositionDb = dataPosition,
                thisPositionDb = NO_POSITION,
            )
            val updatePositionDb = PositionDb(
                id = USER_ID,
                position = dataPosition.position,
                timeFormat = Format.formatTimeFromEpoch(time)
            )
            positionDao.insert(updatePositionDb)
        }
    }

    suspend fun getUserPosition(): PositionDb? {
        return positionDao.getPosition(USER_ID)
    }


    override suspend fun getCityFromSearch(searchCity: SearchCity): City {
        val cityDto = apiService.getCityDto(city = searchCity.name)
        return mapper.mapperCityDtoToEntityCity(cityDto, context, searchCity.id)
    }

    override suspend fun addNewCity(city: City) {

        val positionCity = city.location.position
        val datePosition = locationDao.checkCity(positionCity) ?: NO_POSITION
        val time = System.currentTimeMillis()

        val thisPositionDb =
            PositionDb(city.location.locationId, positionCity, formatTimeFromEpoch(time))
        writingAPItoDatabase(datePosition, thisPositionDb)
    }

    suspend fun deletePosition(positionId: Int) {
        positionDao.deletePositions(positionId)
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
                    writingAPItoDatabase(datePositionDb, thisPositionDb)
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
                    value = forecastDaysList.map {
                        mapper.mapperForecastCityDbToEntityForecastCityDays(it)
                    }
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

    fun getCity(): LiveData<List<City>> {
        val cityLiveData = MediatorLiveData<List<City>>()

        val locationSource = locationDao.getAllLocationsLiveData()
        val currentSource = currentDao.getCurrents()
        val forecastDaySource = forecastDayDao.getForecastDay()

        fun updateCityList() {
            val locationList = locationSource.value
            val currenList = currentSource.value
            val forecastDayList = forecastDaySource.value

            if (locationList != null && currenList != null && forecastDayList != null &&
                locationList.size == currenList.size && locationList.size == forecastDayList.size
            ) {

                val cityList = mutableListOf<City>()
                for (i in locationList.indices) {
                    val location = mapper.mapperLocationDbToEntityLocation(locationList[i])
                    val current = mapper.mapperCurrentDbToEntityCurrent(currenList[i], context)
                    val forecastDays =
                        mapper.mapperForecastCityDbToEntityForecastCityDays(forecastDayList[i])
                    cityList.add(City(location, current, forecastDays))
                }
                cityLiveData.value = cityList
            } else {
                cityLiveData.value = emptyList()
            }
        }

        cityLiveData.addSource(locationSource) { updateCityList() }
        cityLiveData.addSource(currentSource) { updateCityList() }
        cityLiveData.addSource(forecastDaySource) { updateCityList() }

        return cityLiveData
    }

    private suspend fun writingAPItoDatabase(
        datePositionDb: PositionDb?,
        thisPositionDb: PositionDb,
    ) {
        Log.d(
            "RepositoryDataImpl_Log",
            "writingAPItoDatabase: datePositionDb: $datePositionDb | thisPositionDb: $thisPositionDb"
        )

        try {
            if (checkingForUpdates(datePositionDb, thisPositionDb)) {
                Log.d(
                    "RepositoryDataImpl_Log",
                    "writingAPItoDatabase: datePositionDb: $datePositionDb | thisPositionDb: $thisPositionDb"
                )

                val city = apiService.getCityDto(city = thisPositionDb.position)

                locationDao.insert(
                    mapper.mapperCityDtoToLocationDb(
                        id = thisPositionDb.id,
                        cityDto = city,
                        position = "${city.locationDto.lat},${city.locationDto.lon}",
                        timeUpdate = thisPositionDb.timeFormat
                    )
                )
                currentDao.insert(
                    mapper.mapperCityDtoToCurrentDb(
                        id = thisPositionDb.id, cityDto = city
                    )
                )
                val forecastItem = mapper.mapperCityDtoToForecastDaysDb(
                    id = thisPositionDb.id, cityDto = city
                )
                forecastDayDao.insert(forecastItem)



                Log.d("Repository_Log", "saveUserLocation = finish")
            } else {
                Log.d("Repository_Log", "not_update")
            }
        } catch (_: Exception) {
        }
    }


    private fun checkingForUpdates(
        datePositionDb: PositionDb?,
        thisPositionDb: PositionDb
    ): Boolean {
        if (datePositionDb != null) {
            val thisHour = formatTimeByHour(thisPositionDb.timeFormat)
            val dataHour = formatTimeByHour(datePositionDb.timeFormat)
            val update = datePositionDb.position != thisPositionDb.position || thisHour != dataHour

            Log.d(
                "RepositoryDataImpl",
                "checkingForUpdates: datePosition: ${datePositionDb.position}," +
                        " thisPosition ${thisPositionDb.position} , ${datePositionDb.position != thisPositionDb.position} "
            )
            Log.d(
                "RepositoryDataImpl",
                "checkingForUpdates: thisHour:  $thisHour," +
                        " dataHour $dataHour , ${thisHour == dataHour}"
            )
            Log.d(
                "RepositoryDataImpl",
                "update: $update"
            )
            return (update)
        } else {
            return true
        }
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
        const val USER_ID = 0
        private val NO_POSITION = PositionDb(-1, "", "")
    }


}
