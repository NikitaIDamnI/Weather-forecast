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
import com.example.weatherforecastapp.domain.StateCity
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Current
import com.example.weatherforecastapp.domain.models.ForecastDayCity
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d(TAG, "exceptionHandler: $throwable")
    }
    private val repositoryCoroutineScope = CoroutineScope(Dispatchers.IO + exceptionHandler)


    @OptIn(FlowPreview::class)
    private val cityFlow: Flow<StateCity> = channelFlow {
        locationDao.getLocationsFlow()
            .onStart {
                weatherUpdate()
                send(StateCity.Loading(true))

            }
            .catch {
                send(StateCity.Loading(false))
                buildCity().collect {
                    if (it.isNotEmpty()) {
                        send(StateCity.Cities(it))
                    } else {
                        send(StateCity.Empty)
                    }
                }
            }
            .debounce(500)
            .collectLatest { locationFlow ->
                if (locationFlow.isNotEmpty()) {
                    buildCity().collect { cityList ->
                        if (cityList.isNotEmpty()) {
                            send(StateCity.Cities(cityList))
                        }
                    }
                } else {
                    send(StateCity.Empty)
                }
            }

    }

     fun getCitiesFlow(): SharedFlow<StateCity> {
        return cityFlow.stateIn(
            repositoryCoroutineScope, SharingStarted.Lazily, StateCity.Loading(false)
        )
    }


    private suspend fun buildCity(): Flow<List<City>> = flow {
        val locationFlow = locationDao.getLocationsFlow()
        combine(
            locationFlow,
            currentDao.getCurrentsFlow(),
            forecastDayDao.getForecastDayFlow()
        ) { locationList, currentList, forecastDayList ->
            val cityList = mutableListOf<City>()

            // Проверяем, что все списки имеют одинаковую длину
            val minSize = minOf(locationList.size, currentList.size, forecastDayList.size)

            if (minSize > 0 && locationList.isNotEmpty()) {
                for (index in 0 until minSize) {
                    val location = mapper.mapperLocationDbToEntityLocation(locationList[index])
                    val current = mapper.mapperCurrentDbToEntityCurrent(currentList[index], context)
                    val forecastDay = mapper.mapperForecastCityDbToEntityForecastCityDays(forecastDayList[index])
                    cityList.add(City(location, current, forecastDay))
                }
            }
            cityList
        }.collect { cityList ->
            emit(cityList)
        }
    }


    suspend fun allLocations(): List<Location> {
        return locationDao.getAllLocations().map { mapper.mapperLocationDbToEntityLocation(it) }
    }

    override suspend fun saveUserPosition(positionDb: PositionDb): Boolean {
        val datePosition = positionDao.getPosition(USER_ID) ?: NO_POSITION
        if (datePosition.position != positionDb.position) {
            positionDao.insert(positionDb)
            writingAPItoDatabase(datePosition, positionDb)
        }
        return true
    }

    suspend fun updateUserPosition() {
        val dataPosition = getUserPosition()
        val userLocation = locationDao.getUserLocation()
        if (dataPosition != null) {
            var thisPositionDb = dataPosition
            if (userLocation != null) {
                thisPositionDb = PositionDb(
                    0,
                    position = userLocation.position,
                    timeFormat = userLocation.localtime
                )
            }
            val time = System.currentTimeMillis()
            if (dataPosition.position != thisPositionDb.position) {
                writingAPItoDatabase(
                    datePositionDb = dataPosition,
                    thisPositionDb = thisPositionDb,
                )
                val updatePositionDb = PositionDb(
                    id = USER_ID,
                    position = dataPosition.position,
                    timeFormat = Format.formatTimeFromEpoch(time)
                )
                positionDao.insert(updatePositionDb)
            }
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
            PositionDb(city.location.locationId, positionCity, Format.formatTimeFromEpoch(time))
        writingAPItoDatabase(datePosition, thisPositionDb)
    }

    suspend fun deletePosition(positionId: Int) {
        positionDao.deletePositions(positionId)
    }


    override suspend fun weatherUpdate(){

        Log.d("RepositoryDataImpl_Log", "weatherUpdate: START")

        val dataListLocation = locationDao.getAllLocations()
        val time = System.currentTimeMillis()
        Log.d("RepositoryDataImpl_Log", "weatherUpdate: dataListLocation : $dataListLocation")

        if (dataListLocation.isNotEmpty()) {
            for (location in dataListLocation) {
                val thisPositionDb =
                    PositionDb(
                        location.id,
                        location.position,
                        Format.formatTimeFromEpoch(time)
                    )
                val datePositionDb = PositionDb(
                    location.id,
                    location.position,
                    timeFormat = location.last_updated
                )
                Log.d("RepositoryDataImpl_Log", "weatherUpdate: Location : $location")

                if (checkingForUpdates(datePositionDb)) {
                    Log.d("RepositoryDataImpl_Log", "weatherUpdate: UPDATE Location : $location")
                    writingAPItoDatabase(datePositionDb, thisPositionDb)
                }
            }

        } else {
            Log.d("RepositoryDataImpl_Log", "weatherUpdate: NO_UPDATE")
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
            addSource(forecastDayDao.getForecastDayLiveData()) { forecastDaysList ->
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

    override fun getLocations(): Flow<List<Location>> {
        return locationDao.getLocationsFlow()
            .map { it.map { mapper.mapperLocationDbToEntityLocation(it) } }
    }

    override fun getCurrentDays(): LiveData<List<Current>> {
        return MediatorLiveData<List<Current>>().apply {
            addSource(currentDao.getCurrentsLiveData()) { currentList ->
                if (currentList != null) {
                    value = currentList.map { mapper.mapperCurrentDbToEntityCurrent(it, context) }
                } else {
                    emptyList<Current>()
                }
            }
        }
    }



    private suspend fun writingAPItoDatabase(
        datePositionDb: PositionDb?,
        thisPositionDb: PositionDb,
    ) {
        if (checkingForUpdates(datePositionDb)) {
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
        }
    }


    private fun checkingForUpdates(
        datePositionDb: PositionDb?,
    ): Boolean {
        if (datePositionDb != null) {
            val time = Format.formatTimeFromEpoch(System.currentTimeMillis())
            val dataHour = Format.formatTimeByHour(datePositionDb.timeFormat)
            val thisHour = Format.formatTimeByHour(time)
            val update = thisHour != dataHour
            return (update)
        } else {
            return true
        }
    }


    companion object {
        const val TAG = "RepositoryDataImpl_Log"
        const val USER_ID = 0
        private val NO_POSITION = PositionDb(-1, "", "")
    }


}
