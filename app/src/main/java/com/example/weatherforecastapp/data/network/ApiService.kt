package com.example.testapi.network

import com.example.testapi.network.model.curentModels.WeatherResponseDto
import com.example.testapi.network.model.searchCityModels.SearchCityDto
import com.example.weatherforecastapp.data.network.model.CityDto
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("current.json")
    suspend fun currentWeather(
        @Query("key") key: String = API_KEY,
        @Query("q") city: String,
    ): WeatherResponseDto

    @GET("search.json")
    suspend fun searchCity(
        @Query("key") key: String = API_KEY,
        @Query("q") city: String,
    ): List<SearchCityDto>

    @GET("forecast.json")
    suspend fun forecastDays(
        @Query("key") key: String = API_KEY,
        @Query("days") days : Int = NUMBER_OF_DAYS,
        @Query("q") city: String
    ):CityDto


    companion object {
        private const val API_KEY = "e5d3e604cfc14e6d88d72248240503"
        private const val NUMBER_OF_DAYS = 3
    }


}