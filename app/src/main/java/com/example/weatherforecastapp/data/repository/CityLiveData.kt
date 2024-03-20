package com.example.weatherforecastapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Current
import com.example.weatherforecastapp.domain.models.ForecastDay
import com.example.weatherforecastapp.domain.models.Location

class CityLiveData(
    private val id: Int,
    private val locationLiveData: LiveData<Location>,
    private val currentLiveData: LiveData<Current>,
    private val forecastLiveData: LiveData<List<ForecastDay>>
) : MediatorLiveData<City>() {

    init {
        addSource(locationLiveData) { location ->
            val current = currentLiveData.value
            val forecastDays = forecastLiveData.value
            if (current != null && forecastDays != null) {
                value = City(id, location, current, forecastDays)
            }
        }

        addSource(currentLiveData) { current ->
            val location = locationLiveData.value
            val forecastDays = forecastLiveData.value
            if (location != null && forecastDays != null) {
                value = City(id, location, current, forecastDays)
            }
        }

        addSource(forecastLiveData) { forecastDays ->
            val location = locationLiveData.value
            val current = currentLiveData.value
            if (location != null && current != null) {
                value = City(id, location, current, forecastDays)
            }
        }
    }
}