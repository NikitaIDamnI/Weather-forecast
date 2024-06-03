package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.models.StateCity
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import javax.inject.Inject

class UseCaseGetPreviewCity @Inject constructor(
    private val repositoryData: RepositoryData
){
     suspend operator fun invoke(searchCity: SearchCity): StateCity.PreviewCity {
        return repositoryData.getPreviewCity(searchCity)

    }
}

