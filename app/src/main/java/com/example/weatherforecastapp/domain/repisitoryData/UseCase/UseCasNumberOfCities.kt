package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import javax.inject.Inject

class UseCasNumberOfCities @Inject constructor(
    private val repositoryData: RepositoryData
){
    suspend operator fun invoke(): Int{
       return repositoryData.numberOfCities()
    }
}

