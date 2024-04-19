package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import javax.inject.Inject

class UseCaseGetCityFromSearch @Inject constructor(
    private val repositoryData: RepositoryData
) {
    suspend operator fun invoke(searchCity: SearchCity): City {
        return repositoryData.getCityFromSearch(searchCity)

    }
}