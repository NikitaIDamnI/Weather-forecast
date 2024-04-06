package com.example.weatherforecastapp.presentation.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.models.City
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


    val listLocation = MutableLiveData<List<Location>>()

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
    fun addCity(searchCity: SearchCity) {
        viewModelScope.launch {
            useCaseAddCity(searchCity)
            getCities()
        }
    }

     fun getCities() {
        viewModelScope.launch {
            listLocation.value = useCaseGetLocations()
        }
    }

    fun deleteCity(location: Location) {
        viewModelScope.launch {
            useCasDeleteCity(location.positionId)
            getCities()
        }

    }
    fun getWeatherHour24(localTime: String,forecastDay: List<ForecastDay>): List<ForecastHour> {
        val startIndex = localTime.split(":")[0].toInt()
        val oldListTo24 = forecastDay[0].forecastHour
        val oldListNextDay = forecastDay[1].forecastHour
        val newListTo24 = oldListTo24.slice(startIndex until oldListTo24.size)
        val newListNextDay = oldListNextDay.slice(0 until startIndex + 1)
        return mutableListOf<ForecastHour>().apply {
            addAll(newListTo24)
            addAll(newListNextDay)
        }
    }
}