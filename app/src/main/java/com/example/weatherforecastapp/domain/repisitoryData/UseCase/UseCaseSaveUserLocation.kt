package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.data.database.models.PositionDb
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import javax.inject.Inject

class UseCaseSaveUserLocation @Inject constructor(
    private val repositoryData: RepositoryData
){
    suspend operator fun invoke(positionDb: PositionDb){
        repositoryData.saveUserPosition(positionDb)

    }
}

