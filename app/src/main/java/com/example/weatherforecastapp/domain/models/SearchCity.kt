package com.example.weatherforecastapp.domain.models

data class SearchCity(
    val id: Int,
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    )
