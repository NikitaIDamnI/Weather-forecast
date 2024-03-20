package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData

class UseCaseAddNewCity (
    private val repositoryData: RepositoryData
){
    suspend operator fun invoke(searchCity: SearchCity){
        repositoryData.addNewCity(searchCity)

    }
}

