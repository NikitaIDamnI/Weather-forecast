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
import com.example.weatherforecastapp.domain.models.ForecastDaysCity
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
                writingAPItoDatabase(datePosition, positionDb)
            }
        } catch (_: Exception) {

        }
        Log.d("Repository_Log", "saveUserLocation ")
        return true
    }

    suspend fun updateUserPosition(position: PositionDb) {
        Log.d("RepositoryDataImpl_Log", "updateUserPosition: $position ")
        writingAPItoDatabase(
            datePositionDb = NO_POSITION,
            thisPositionDb = position,
        )
    }

    suspend fun getUserPosition(): PositionDb? {
        return positionDao.getPosition(CURRENT_LOCATION_ID)
    }


    override suspend fun getCityFromSearch(searchCity: SearchCity): City {
        val cityDto = apiService.getCityDto(city = searchCity.name)
        return mapper.mapperCityDtoToEntityCity(cityDto, context, searchCity.id)
    }

    override suspend fun addNewCity(city: City) {
        val positionsId = getPositionId()

        val positionCity = city.location.position
        val datePosition = locationDao.checkCity(positionCity) ?: NO_POSITION
        val time = System.currentTimeMillis()

        val thisPositionDb =
            PositionDb(city.location.locationId, positionCity, formatTimeFromEpoch(time))

        Log.d("Repository_Log", "thisPosition $thisPositionDb , datePosition | $datePosition")
        writingAPItoDatabase(datePosition, thisPositionDb)
    }

    suspend fun deletePosition(positionId: Int) {
        positionDao.deletePositions(positionId)
    }

    private suspend fun getPositionId(): Int {
        var sumPositions = locationDao.getSumPosition()
        if (sumPositions == NOT_POSITIONS) {
            return POSITION_ID_NEXT
        } else {
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

    override fun getForecastDaysCity(): LiveData<List<ForecastDaysCity>> {
        return MediatorLiveData<List<ForecastDaysCity>>().apply {
            addSource(forecastDayDao.getForecastDay()) { forecastDaysList ->
                if (forecastDaysList != null) {
                    value = forecastDaysList.map {
                        mapper.mapperForecastCityDbToEntityForecastCityDays(it)
                    }
                } else {
                    emptyList<ForecastDaysCity>()
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


    fun getCityLiveData(coroutineScope: CoroutineScope): LiveData<List<City>> {
        val cityLiveData = MediatorLiveData<List<City>>()

        // Добавляем источник для списка локаций
        cityLiveData.addSource(locationDao.getAllLocationsLiveData()) { locationList ->
            coroutineScope.launch {
                try {
                    // Получаем список текущих данных и прогнозов
                    val currentList = currentDao.getCurrentsAll()
                    val forecastDaysList = forecastDayDao.getForecastDayAll()

                    // Обрабатываем данные только если все списки не пусты
                    if (locationList.isNotEmpty() && currentList.isNotEmpty() && forecastDaysList.isNotEmpty()) {
                        val cityList = mutableListOf<City>()
                        for (position in locationList.indices) {
                            val locationEntity =
                                mapper.mapperLocationDbToEntityLocation(locationList[position])
                            val current =
                                mapper.mapperCurrentDbToEntityCurrent(
                                    currentList[position],
                                    context
                                )
                            val forecastDays =
                                mapper.mapperForecastCityDbToEntityForecastCityDays(forecastDaysList[position])

                            val city = City(locationEntity, current, forecastDays)
                            cityList.add(city)
                        }
                        cityLiveData.postValue(cityList)
                    } else {
                        cityLiveData.postValue(emptyList())
                    }
                }catch (e: RuntimeException){

                }
            }
        }

        return cityLiveData
    }


    private suspend fun writingAPItoDatabase(
        datePositionDb: PositionDb,
        thisPositionDb: PositionDb,
    ) {
        try {
            if (checkingForUpdates(datePositionDb, thisPositionDb)) {
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
