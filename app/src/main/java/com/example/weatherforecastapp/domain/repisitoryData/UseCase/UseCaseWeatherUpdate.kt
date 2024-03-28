package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData

class UseCaseWeatherUpdate(
    private val repositoryData: RepositoryData
) {

     suspend operator fun invoke() {
        return repositoryData.weatherUpdate()

    }

}
