package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.models.StateCity
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class UseCaseGetCities @Inject constructor(
    private val repositoryData: RepositoryData
){
     operator fun invoke(): SharedFlow<StateCity> {
       return repositoryData.getCities()
    }
}

