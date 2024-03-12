package com.example.weatherforecastapp.domain.repisitoryData

interface RepositoryData {

    suspend fun saveUserLocation(location: String)
    suspend fun weatherUpdate(city: String)
    suspend fun searchCity(city: String)
    suspend fun deleteCity (cityId: Int)

}