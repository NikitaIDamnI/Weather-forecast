package com.example.weatherforecastapp.domain.repisitoryData

import androidx.lifecycle.LiveData
import com.example.weatherforecastapp.data.database.models.Position
import com.example.weatherforecastapp.domain.models.Current
import com.example.weatherforecastapp.domain.models.ForecastDay
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity

interface RepositoryData {

    suspend fun saveUserLocation(position: Position): Boolean

    suspend fun addNewCity(searchCity: SearchCity)
    suspend fun searchCity(city: String): List<SearchCity>
    suspend fun deleteCity(cityId: Int)
    suspend fun getLocations(): LiveData<List<Location>>
    fun gerLocation(id: Int): LiveData<Location>
    fun getCurrentDay(id: Int): LiveData<Current>
    fun forecastDas(id: Int): LiveData<List<ForecastDay>>
    suspend fun numberOfCities(): Int
}