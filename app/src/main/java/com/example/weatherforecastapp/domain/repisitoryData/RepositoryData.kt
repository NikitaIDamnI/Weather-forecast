package com.example.weatherforecastapp.domain.repisitoryData

import androidx.lifecycle.LiveData
import com.example.weatherforecastapp.data.database.models.PositionDb
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Current
import com.example.weatherforecastapp.domain.models.ForecastDaysCity
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity

interface RepositoryData {

    suspend fun saveUserPosition(positionDb: PositionDb): Boolean

    suspend fun addNewCity(city: City)
    suspend fun getCityFromSearch(searchCity: SearchCity): City
    suspend fun searchCity(city: String): List<SearchCity>
    suspend fun deleteCity(positionId: Int)
    fun getLocations(): LiveData<List<Location>>
    suspend fun getUserLocation(): Location
    suspend fun weatherUpdate()
    fun getCurrentDays(): LiveData<List<Current>>
    fun getForecastDaysCity(): LiveData<List<ForecastDaysCity>>
    fun getSizePager(): LiveData<Int>
    suspend fun numberOfCities(): Int
}