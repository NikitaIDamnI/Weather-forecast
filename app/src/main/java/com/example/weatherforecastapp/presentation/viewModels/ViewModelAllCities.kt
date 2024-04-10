package com.example.weatherforecastapp.presentation.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseGetLocations
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseSearchCity
import kotlinx.coroutines.launch

class ViewModelAllCities(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RepositoryDataImpl(application)
    private val useCaseGetLocations = UseCaseGetLocations(repository)
    private val useCaseSearchCity = UseCaseSearchCity(repository)
    private val useCaseAddCity = UseCaseAddNewCity(repository)
    private val useCasDeleteCity = UseCasDeleteCity(repository)

    val city = MutableLiveData<City>()


    val listLocation =  useCaseGetLocations()

    val searchCityList = MutableLiveData<List<SearchCity>>()


    fun searchCity(city: String) {
        viewModelScope.launch {
            searchCityList.value = useCaseSearchCity(city)
            //Log.d("ViewModelAllCities_Log", "searchCity: $city ")
        }

    }

    fun previewCity(searchCity: SearchCity){
        viewModelScope.launch {
            val citySearch = repository.getCityFromSearch(searchCity)
            Log.d("ViewModelAllCities_Log", "previewCity: ${citySearch.location.name}")
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
    fun getWeatherHour24(forecastDay: List<ForecastDay>,timeLocation: String): List<ForecastHour> {
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
            val index = sunsetHour - newListNextDay.size
            newListNextDay.add(sunriseHour+1, sunrise)
        }

        val weatherHour24 = mutableListOf<ForecastHour>().apply {
            addAll(newListTo24)
            addAll(newListNextDay)
        }

        return weatherHour24
    }
}