package com.example.weatherforecastapp.presentation.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
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
import com.example.weatherforecastapp.presentation.checkingСonnections.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelWeather @Inject constructor(
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

    val state = MutableLiveData<State>()

    private var firstWeatherUpdate = true
    private var firstUserUpdate = true


    val cities = repositoryDataImpl.getCitiesFlow()


    private var _previewCity = MutableStateFlow<StateCity>(StateCity.Empty)
    val previewCity: StateFlow<StateCity>
        get() = _previewCity


    val listLocation = repositoryDataImpl.getLocations()

    var sizeCity = getSizePager()

    val shortNotifications: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)

    init {
        viewModelScope.launch {
            cities.collect {
                when (it) {
                    is StateCity.Loading -> {
                        Log.d("ViewModel_Log", "StateCity.Loading : $it ")
                        val update = it.stateLoading ?: false
                        firstWeatherUpdate = !update
                    }

                    else -> {

                    }
                }
            }
        }
    }

    suspend fun searchCity(city: String): List<SearchCity> {
        return useCaseSearchCity(city)
    }

    fun checkCity(listLocation: List<Location>, searchCity: SearchCity): Boolean {
        if (listLocation == emptyList<Location>()) {
            return false
        } else {
            val position = "${searchCity.lat},${searchCity.lon}"

            Log.d("ViewModelAllCities_Log", "searchCity: $searchCity ")
            Log.d("ViewModelAllCities_Log", "listLocation: ${listLocation[0]} ")
            val checkCity =
                listLocation[0].name == searchCity.name && listLocation[0].country == searchCity.country && listLocation[0].region == searchCity.region
            Log.d("ViewModelAllCities_Log", "checkCity: $checkCity ")

            return listLocation.any {
                it.position == position
            }
        }
    }

    fun addPreviewCity(searchCity: SearchCity) {
        viewModelScope.launch {
            val city = repositoryDataImpl.getCityFromSearch(searchCity)
            val listLocation = repositoryDataImpl.allLocations()
            val addedStatus = checkCity(listLocation,searchCity)

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

    fun getState(newState: State) {
        state.value = newState
        Log.d("ViewModelNetworkStatus", "state: $newState")
    }

    fun openNotification() {
        shortNotifications.value = false
    }

    fun closeNotification() {
        shortNotifications.value = true
    }


//    fun updateUserLocation() {
//        Log.d("RepositoryDataImpl_Log", "firstUserUpdate: $firstUserUpdate ")
//        if (firstUserUpdate) {
//            viewModelScope.launch {
//                repositoryDataImpl.updateUserPosition()
//            }
//            firstUserUpdate = false
//        }
//    }


    companion object {
        const val USER_POSITION = 0
    }
}