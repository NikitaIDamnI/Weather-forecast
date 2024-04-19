package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import androidx.lifecycle.LiveData
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import javax.inject.Inject

class UseCaseGetSizePager @Inject constructor(
    private val repositoryData: RepositoryData
) {
    operator fun invoke(): LiveData<Int> {
        return repositoryData.getSizePager()
    }
}

