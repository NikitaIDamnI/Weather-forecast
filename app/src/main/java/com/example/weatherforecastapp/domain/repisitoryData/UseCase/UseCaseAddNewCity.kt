package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData

class UseCaseAddNewCity (
    private val repositoryData: RepositoryData
){
    suspend operator fun invoke(city: City){
        repositoryData.addNewCity(city)

    }
}

