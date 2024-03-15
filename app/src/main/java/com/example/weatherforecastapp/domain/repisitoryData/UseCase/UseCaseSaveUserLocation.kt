package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.data.database.models.Position
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData

class UseCaseSaveUserLocation (
    private val repositoryData: RepositoryData
){
    operator suspend fun invoke(position: Position){
        repositoryData.saveUserLocation(position)

    }
}

