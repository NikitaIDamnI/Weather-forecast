package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData

class UseCaseGetLocations (
    private val repositoryData: RepositoryData
){
    suspend operator fun invoke(): List<Location> {
        return repositoryData.getLocations()

    }
}

