package com.example.weatherforecastapp.domain.repositoryLocation.UseCase

import com.example.weatherforecastapp.domain.repositoryLocation.LocationRepository

class UseCaseCheckLocation (
    private val repository: LocationRepository
) {
    operator fun invoke(){
        repository.checkLocation()
    }
}