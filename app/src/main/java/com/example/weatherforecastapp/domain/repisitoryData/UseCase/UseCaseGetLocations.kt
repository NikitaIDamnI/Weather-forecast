package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import androidx.lifecycle.LiveData
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UseCaseGetLocations @Inject constructor(
    private val repositoryData: RepositoryData
){
     operator fun invoke(): Flow<List<Location>> {
        return repositoryData.getLocations()

    }
}

