package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import javax.inject.Inject

class UseCaseGetUserLocation @Inject constructor(
    private val repositoryData: RepositoryData
) {
   suspend operator fun invoke(): Location{
        return repositoryData.getUserLocation()
    }
}