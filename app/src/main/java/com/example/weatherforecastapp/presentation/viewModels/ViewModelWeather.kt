package com.example.weatherforecastapp.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.data.gps.LocationRepositoryImpl
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCasNumberOfCities
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseGetCity
import com.example.weatherforecastapp.domain.repositoryLocation.UseCase.UseCaseCheckLocation
import kotlinx.coroutines.launch

class ViewModelWeather(private val application: Application) : AndroidViewModel(application) {

    private val repository = RepositoryDataImpl(application)
    private val repositoryLocal = LocationRepositoryImpl(application)
    private val useCaseGetCity = UseCaseGetCity(repository)
    private val useCaseCheckLocation = UseCaseCheckLocation(repositoryLocal)
    private val useCasNumberOfCities = UseCasNumberOfCities(repository)


    private val listCity = mutableListOf<LiveData<City>>()
    val city = MutableLiveData<City>()
    var sizeCity = MutableLiveData<Int>(MIN_SIZE_CITY)

    init {
        numberOfCities()

    }

    fun getCity(cityId: Int) {
        viewModelScope.launch {
            city.value = useCaseGetCity.invoke(cityId).value
        }
    }

    private fun numberOfCities() {
        viewModelScope.launch {
            sizeCity.value = useCasNumberOfCities.invoke()
        }

    }


    fun checkLocation() {
        useCaseCheckLocation.invoke()
    }

    companion object {
        const val MIN_SIZE_CITY = 1
    }
}