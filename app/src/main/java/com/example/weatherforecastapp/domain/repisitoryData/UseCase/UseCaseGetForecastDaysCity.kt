package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import androidx.lifecycle.LiveData
import com.example.weatherforecastapp.domain.models.ForecastDaysCity
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import javax.inject.Inject

class UseCaseGetForecastDaysCity @Inject constructor(
    private val repositoryData: RepositoryData
){
     operator fun invoke(): LiveData<List<ForecastDaysCity>> {
        return repositoryData.getForecastDaysCity()

    }
}

