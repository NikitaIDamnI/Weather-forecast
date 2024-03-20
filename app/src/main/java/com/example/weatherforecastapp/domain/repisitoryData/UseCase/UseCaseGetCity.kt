package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import androidx.lifecycle.LiveData
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData

class UseCaseGetCity(
    private val repositoryData: RepositoryData
) {
     operator fun invoke(cityId: Int): LiveData<City> {
        return repositoryData.getCity(cityId)

    }
}

