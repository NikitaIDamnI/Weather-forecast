package com.example.weatherforecastapp.domain.repisitoryData

import androidx.lifecycle.LiveData
import com.example.weatherforecastapp.data.database.models.Position
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Current
import com.example.weatherforecastapp.domain.models.ForecastDay
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity

interface RepositoryData {

    suspend fun saveUserLocation(position: Position): Boolean

    suspend fun addNewCity(city: City)
    suspend fun searchCity(city: String): List<SearchCity>
    suspend fun deleteCity(positionId: Int)
    fun getLocations(): LiveData<List<Location>>

    suspend fun getUserLocation(): Location
    fun getLocation(id: Int): LiveData<Location>
    suspend fun weatherUpdate()
    fun getCurrentDay(id: Int): LiveData<Current>
    fun getForecastDas(id: Int): LiveData<List<ForecastDay>>
    suspend fun numberOfCities(): Int
}