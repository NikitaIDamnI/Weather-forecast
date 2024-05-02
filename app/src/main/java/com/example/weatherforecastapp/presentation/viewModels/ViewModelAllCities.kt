package com.example.weatherforecastapp.presentation.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Condition
import com.example.weatherforecastapp.domain.models.ForecastDay
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
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelAllCities @Inject constructor(
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


    val city = MutableLiveData<City>()
    val listLocation = useCaseGetLocations()
    val searchCityList = MutableLiveData<List<SearchCity>>()
    var sizeCity = getSizePager()
    val internetCondition =
        MutableLiveData<Boolean>(true)
    val locationCondition =
        MutableLiveData<Boolean>(true)


    fun searchCity(city: String) {
        viewModelScope.launch {
            searchCityList.value = useCaseSearchCity(city)
        }

    }

    fun checkCity(listLocation: List<Location>, position: String): Boolean {
        return listLocation.any { it.position == position }
    }

    fun previewCity(searchCity: SearchCity) {
        viewModelScope.launch {
            val citySearch = useCaseGetCityFromSearch(searchCity)
            city.value = citySearch
        }
    }

    fun addCity(city: City) {
        viewModelScope.launch {
            useCaseAddCity(city)
        }
    }

    fun deleteCity(location: Location) {
        viewModelScope.launch {
            useCasDeleteCity(location.positionId)
        }
    }

    fun weatherUpdate() {
        viewModelScope.launch {
            weatherUpdate.invoke()
        }
    }

    fun checkLocationCondition(condition: Boolean){
        locationCondition.value = condition
    }
    fun checkInternetCondition(condition: Boolean){
        internetCondition.value = condition
    }
    fun checkLocation(context: Context) {
        useCaseCheckLocation.invoke(context)
    }

    fun getWeatherHour24(forecastDay: List<ForecastDay>, timeLocation: String): List<ForecastHour> {
        val astro = forecastDay[0].astro
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

        val startIndex = timeLocation.split(":")[0].toInt()
        val oldListTo24 = forecastDay[0].forecastHour
        val oldListNextDay = forecastDay[1].forecastHour

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

        if (sunriseHour <= newListNextDay.size) {
            newListNextDay.add(sunriseHour + 1, sunrise)
        }

        val weatherHour24 = mutableListOf<ForecastHour>().apply {
            addAll(newListTo24)
            addAll(newListNextDay)
        }

        return weatherHour24
    }




    fun updateUserLocation() {
        viewModelScope.launch {
            val userPosition = repositoryDataImpl.getUserPosition()
            repositoryDataImpl.updateUserPosition(userPosition)
        }
    }
}