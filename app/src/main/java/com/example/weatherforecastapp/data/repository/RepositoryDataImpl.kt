package com.example.weatherforecastapp.data.repository

import android.util.Log
import com.example.testapi.network.ApiService
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import javax.inject.Inject

class RepositoryDataImpl @Inject constructor(
    val apiService: ApiService
): RepositoryData {

    override suspend  fun saveUserLocation(location: String) {
        //apiService.currentWeather(city = location)
        Log.d("Repository_Log","save|$location")
    }

    override suspend fun weatherUpdate(city: String) {
        TODO("Not yet implemented")
    }

    override suspend fun searchCity(city: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCity(cityId: Int) {
        TODO("Not yet implemented")
    }
}