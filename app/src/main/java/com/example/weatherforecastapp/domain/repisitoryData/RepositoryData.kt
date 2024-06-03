package com.example.weatherforecastapp.domain.repisitoryData

import com.example.weatherforecastapp.data.database.models.PositionDb
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.models.StateCity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface RepositoryData {
    fun getCities(): SharedFlow<StateCity>
    suspend fun saveUserPosition(positionDb: PositionDb): Boolean
    suspend fun addNewCity(city: City)
    suspend fun getCityFromSearch(searchCity: SearchCity): City
    suspend fun searchCity(city: String): List<SearchCity>
    suspend fun deleteCity(positionId: Int)
    fun getLocations(): Flow<List<Location>>
    suspend fun weatherUpdate()
    suspend fun updateUserPosition()
    suspend fun getPreviewCity(searchCity: SearchCity): StateCity.PreviewCity
}