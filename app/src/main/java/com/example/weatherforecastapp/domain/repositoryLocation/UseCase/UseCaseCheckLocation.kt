package com.example.weatherforecastapp.domain.repositoryLocation.UseCase

import android.content.Context
import com.example.weatherforecastapp.domain.repositoryLocation.LocationRepository

class UseCaseCheckLocation (
    private val repository: LocationRepository
) {
    operator fun invoke(context: Context){
        repository.checkLocation(context)
    }
}