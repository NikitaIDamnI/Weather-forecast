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
}