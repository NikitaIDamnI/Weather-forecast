package com.example.weatherforecastapp.domain.repisitoryData

import androidx.lifecycle.LiveData
import com.example.weatherforecastapp.data.database.models.Position
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Location

interface RepositoryData {

    suspend fun saveUserLocation(position: Position)
    suspend fun weatherUpdate(city: String)
    suspend fun searchCity(city: String) : List<String>
    suspend fun deleteCity (cityId: Int)
    fun getCity(cityId: Int): LiveData<City>
    suspend fun getLocation(): List<Location>

}