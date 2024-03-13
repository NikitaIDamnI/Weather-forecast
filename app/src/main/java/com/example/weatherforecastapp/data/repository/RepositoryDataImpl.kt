package com.example.weatherforecastapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.testapi.network.ApiService
import com.example.weatherforecastapp.data.mapper.Mapper
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData

class RepositoryDataImpl (
    val apiService: ApiService,
    val mapper: Mapper
): RepositoryData {

    override suspend  fun saveUserLocation(location: String) {
       // apiService.forecastDays(city = location)
        Log.d("Repository_Log","save|$location")
    }

    override suspend fun weatherUpdate(city: String) {
        Log.d("Repository_Log","weatherUpdate($city)")
    }

    override suspend fun searchCity(city: String): List<String> {
        Log.d("Repository_Log","searchCity($city)")
        return mutableListOf()

    }

    override suspend fun deleteCity(cityId: Int) {
        Log.d("Repository_Log","deleteCity($cityId)")
    }

    override fun getCity(cityId: Int): LiveData<City> {
        TODO("Not yet implemented")
    }

    override suspend fun getLocation(): List<Location> {
        TODO("Not yet implemented")
    }
}