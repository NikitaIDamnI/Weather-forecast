package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import javax.inject.Inject

class UseCaseWeatherUpdate @Inject constructor(
    private val repositoryData: RepositoryData
) {

     suspend operator fun invoke() {
        return repositoryData.weatherUpdate()

    }

}
