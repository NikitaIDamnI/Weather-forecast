package com.example.weatherforecastapp.domain.repositoryLocation.UseCase

import com.example.weatherforecastapp.domain.repositoryLocation.LocationRepository
import javax.inject.Inject

class UseCaseCheckLocation @Inject constructor(
    private val repository: LocationRepository
) {
    operator fun invoke(){
        repository.checkLocation()
    }
}