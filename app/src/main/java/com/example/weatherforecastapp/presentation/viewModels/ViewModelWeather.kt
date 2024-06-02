package com.example.weatherforecastapp.presentation.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.StateCity
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Condition
import com.example.weatherforecastapp.domain.models.ForecastDayCity
import com.example.weatherforecastapp.domain.models.ForecastHour
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCasDeleteCity
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseAddNewCity
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseGetCityFromSearch
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseGetLocations
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseGetSizePager
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseSearchCity
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseWeatherUpdate
import com.example.weatherforecastapp.domain.repositoryLocation.UseCase.UseCaseCheckLocation
import com.example.weatherforecastapp.presentation.checkingСonnections.StateNetwork
import com.example.weatherforecastapp.presentation.checkingСonnections.StateReceiver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelWeather @Inject constructor(
    private val application: Application,
    private val repositoryDataImpl: RepositoryDataImpl,
    private val useCaseGetLocations: UseCaseGetLocations,
    private val useCaseSearchCity: UseCaseSearchCity,
    private val useCaseAddCity: UseCaseAddNewCity,
    private val useCasDeleteCity: UseCasDeleteCity,
    private val useCaseGetCityFromSearch: UseCaseGetCityFromSearch,
    private val getSizePager: UseCaseGetSizePager,
    private val useCaseCheckLocation: UseCaseCheckLocation,
    private val weatherUpdate: UseCaseWeatherUpdate,
) : ViewModel() {

//    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
//        Log.d(TAG, "coroutineContext $coroutineContext :exceptionHandler ${throwable.message} ")
//    }

    private val _stateNetwork = MutableStateFlow(StateNetwork())
    val stateNetwork: StateFlow<StateNetwork> get() = _stateNetwork.asStateFlow()
    val cities: Flow<StateCity> = repositoryDataImpl.getCitiesFlow()


    private var _previewCity = MutableStateFlow<StateCity>(StateCity.Empty)
    val previewCity: StateFlow<StateCity>
        get() = _previewCity

    val listLocation = useCaseGetLocations()

    private var update = false

    init {
        viewModelScope.launch {
            combine(cities, _stateNetwork) { cities, state ->
                Pair(cities, state)
            }.collect { (stateCity, stateNetwork) ->

                if (stateCity is StateCity.Loading) {
                    update = stateCity.state
                }
                Log.d(TAG, "stateCity: $stateCity")
                Log.d(TAG, "stateNetwork: $stateNetwork")
                Log.d(TAG, "update: $update")


                if (stateNetwork.internet && !update) {
                    when (stateCity) {
                        is StateCity.Empty -> {
                            repositoryDataImpl.updateUserPosition()
                            migration(stateNetwork)
                            update = true
                        }

                        is StateCity.Cities -> {
                            Log.d(TAG, "update: $update")
                            weatherUpdate.invoke()
                            update = true
                        }

                        else -> {}
                    }
                }
            }
        }
    }


    private fun migration(stateNetwork: StateNetwork) {
        Log.d(
            TAG, "migration: ${
                stateNetwork.locationPermission
            }"
        )
        if (stateNetwork.locationPermission == false) {

            val newState = _stateNetwork.value.copy(migration = true)
            _stateNetwork.value = newState
        }
    }

    suspend fun searchCity(city: String): List<SearchCity> {
        return useCaseSearchCity(city)
    }

    private fun checkCity(listLocation: List<Location>, searchCity: SearchCity): Boolean {
        if (listLocation == emptyList<Location>()) {
            return false
        } else {
            val position = "${searchCity.lat},${searchCity.lon}"
            val checkCity =
                listLocation[0].name == searchCity.name && listLocation[0].country == searchCity.country
                        && listLocation[0].region == searchCity.region
            return listLocation.any {
                it.position == position
            }
        }
    }

    fun updateState(stateReceiver: StateReceiver) {
        val newState = _stateNetwork.value.copy(
            internet = stateReceiver.internet, location = stateReceiver.location,
            locationPermission = stateReceiver.locationPermission
        )
        _stateNetwork.value = newState
    }

    fun addPreviewCity(searchCity: SearchCity) {
        viewModelScope.launch {
            val city = repositoryDataImpl.getCityFromSearch(searchCity)
            val listLocation = repositoryDataImpl.allLocations()
            val addedStatus = checkCity(listLocation, searchCity)
            _previewCity.emit(StateCity.PreviewCity(city, addedStatus))
        }
    }

    fun resetPreviewCity() {
        _previewCity.value = StateCity.Empty
    }

    fun addCity(city: City) {
        viewModelScope.launch {
            useCaseAddCity(city)
        }
    }

    fun deleteCity(location: Location) {
        viewModelScope.launch {
            useCasDeleteCity(location.locationId)
            if (location.locationId == USER_POSITION) {
                repositoryDataImpl.deletePosition(USER_POSITION)
            }
        }
    }


    fun checkLocation(context: Context) {
        useCaseCheckLocation.invoke(context)
    }


    fun getState(newState: StateReceiver) {
        viewModelScope.launch {
            _stateNetwork.emit(
                StateNetwork(
                    internet = newState.internet,
                    location = newState.location,
                    locationPermission = newState.locationPermission
                )
            )
        }
        Log.d("ViewModelNetworkStatus", "state: $newState")
    }

    fun showNotification(action: Unit) {
        val stateNotification = _stateNetwork.value.shortNotifications
        val newState = _stateNetwork.value.copy(shortNotifications = stateNotification)
        _stateNetwork.value = newState
    }

    fun getListNotificationLocation() {

    }

    fun getWeatherHour24(forecastDayCity: ForecastDayCity): List<ForecastHour> {
        Log.d("ViewModelAllCities_Log", "forecastDayCity: $forecastDayCity ")
        val astro = forecastDayCity.forecastDays[0].astro

        val sunriseHour = astro.sunrise.split(":")[0].toInt()
        val sunsetHour = astro.sunset.split(":")[0].toInt()

        val sunrise = ForecastHour(
            time = astro.sunrise,
            condition = Condition(
                text = "Sunrise",
                icon = R.drawable.sunrise_weather.toString()
            )
        )
        val sunset = ForecastHour(
            time = astro.sunset,
            condition = Condition(
                text = "Sunset",
                icon = R.drawable.sunset_weather.toString()
            )
        )
        val startIndex = forecastDayCity.timeLocation.split(":")[0].toInt()
        val oldListTo24 = forecastDayCity.forecastDays[0].forecastHour
        val oldListNextDay = forecastDayCity.forecastDays[1].forecastHour

        val newListTo24 = oldListTo24.subList(startIndex, oldListTo24.size).toMutableList()
        val newListNextDay = oldListNextDay.subList(0, startIndex + 1).toMutableList()

        if (sunriseHour >= startIndex) {
            val index = sunriseHour - startIndex
            newListTo24.add(index + 1, sunrise)
        }
        if (sunsetHour >= startIndex) {
            val index = sunsetHour - startIndex
            newListTo24.add(index + 1, sunset)
        }
        if (sunriseHour < newListNextDay.size) {
            val index = sunsetHour - newListNextDay.size
            newListNextDay.add(sunriseHour + 1, sunrise)
        }

        val weatherHour24 = mutableListOf<ForecastHour>().apply {
            addAll(newListTo24)
            addAll(newListNextDay)
        }

        return weatherHour24
    }

    companion object {
        const val TAG = "ViewModelWeather_Log"
        const val USER_POSITION = 0
    }
}