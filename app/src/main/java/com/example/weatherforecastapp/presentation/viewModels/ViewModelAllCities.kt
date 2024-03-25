package com.example.weatherforecastapp.presentation.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
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


    val listLocation = MutableLiveData<List<Location>>()

    val searchCityList = MutableLiveData<List<SearchCity>>()

    init {
        getCities()
    }

    fun searchCity(city: String) {
        viewModelScope.launch {
            searchCityList.value = useCaseSearchCity(city)
            Log.d("ViewModelAllCities", "searchCity: $city ")
        }

    }

    fun addCity(searchCity: SearchCity) {
        viewModelScope.launch {
            useCaseAddCity(searchCity)
            getCities()
        }
    }

    private fun getCities() {
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
}