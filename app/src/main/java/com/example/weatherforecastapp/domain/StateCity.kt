package com.example.weatherforecastapp.domain

import com.example.weatherforecastapp.domain.models.City

sealed class StateCity {
    data object Empty: StateCity()
    data class Loading(val state : Boolean) : StateCity()
    data class Cities(val cities: List<City>) : StateCity()

    data class PreviewCity(val city: City,val cityAddedStatus: Boolean ) : StateCity()



}