package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import androidx.lifecycle.LiveData
import com.example.weatherforecastapp.domain.models.Current
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData

class UseCaseGetCurrents (
    private val repositoryData: RepositoryData
){
     operator fun invoke(): LiveData<List<Current>> {
        return repositoryData.getCurrentDays()

    }
}

