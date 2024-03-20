package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData

class UseCasNumberOfCities (
    private val repositoryData: RepositoryData
){
    suspend operator fun invoke(): Int{
       return repositoryData.numberOfCities()
    }
}

