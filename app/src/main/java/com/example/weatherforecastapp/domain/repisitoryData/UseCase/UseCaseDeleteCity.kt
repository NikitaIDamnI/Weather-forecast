package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import javax.inject.Inject

class UseCaseDeleteCity @Inject constructor(
    private val repositoryData: RepositoryData
){
    suspend operator fun invoke(positionId: Int){
        repositoryData.deleteCity(positionId)
    }
}

