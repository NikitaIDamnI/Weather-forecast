package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import javax.inject.Inject

class UseCaseSaveUserLocation @Inject constructor(
    private val repositoryData: RepositoryData
){
    operator suspend fun invoke(location: String){
        repositoryData.saveUserLocation(location)

    }
}

