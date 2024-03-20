package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData

class UseCasDeleteCity (
    private val repositoryData: RepositoryData
){
    suspend operator fun invoke(cityId: Int){
        repositoryData.deleteCity(cityId)
    }
}

