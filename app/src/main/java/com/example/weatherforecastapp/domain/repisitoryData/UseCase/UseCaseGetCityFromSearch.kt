package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData

class UseCaseGetCityFromSearch(
    private val repositoryData: RepositoryData
) {
    suspend operator fun invoke(searchCity: SearchCity): City {
        return repositoryData.getCityFromSearch(searchCity)

    }
}