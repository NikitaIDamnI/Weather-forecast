package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import javax.inject.Inject

class UseCaseSearchCity @Inject constructor(
    private val repositoryData: RepositoryData
) {
    suspend operator fun invoke(city: String): List<SearchCity> {
        return repositoryData.searchCity(city)

    }
}

