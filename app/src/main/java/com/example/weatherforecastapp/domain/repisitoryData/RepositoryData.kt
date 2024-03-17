package com.example.weatherforecastapp.domain.repisitoryData

import androidx.lifecycle.LiveData
import com.example.weatherforecastapp.data.database.models.Position
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity

interface RepositoryData {

    suspend fun saveUserLocation(position: Position)
    suspend fun addNewCity(searchCity: SearchCity)
    suspend fun searchCity(city: String): List<SearchCity>
    suspend fun deleteCity(cityId: Int)
     suspend fun getCity(cityId: Int): LiveData<City>
    suspend fun getLocation(): List<Location>

}