package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import androidx.lifecycle.LiveData
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData

class UseCaseGetSizePager(
    private val repositoryData: RepositoryData
) {
    operator fun invoke(): LiveData<Int> {
        return repositoryData.getSizePager()
    }
}

