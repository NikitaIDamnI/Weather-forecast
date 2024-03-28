package com.example.weatherforecastapp.domain.repositoryLocation.UseCase

import com.example.weatherforecastapp.domain.repositoryLocation.LocationRepository

class UseCaseUpdateDataLocation (
    private val repository: LocationRepository
) {
    operator fun invoke(listener: LocationRepository.LocationUpdateListener){
        repository.setUpdateListener(listener)
    }
}