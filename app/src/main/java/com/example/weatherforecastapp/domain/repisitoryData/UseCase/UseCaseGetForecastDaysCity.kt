package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import androidx.lifecycle.LiveData
import com.example.weatherforecastapp.domain.models.ForecastDayCity
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData

class UseCaseGetForecastDaysCity (
    private val repositoryData: RepositoryData
){
     operator fun invoke(): LiveData<List<ForecastDayCity>> {
        return repositoryData.getForecastDaysCity()

    }
}

