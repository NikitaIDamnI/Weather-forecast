package com.example.weatherforecastapp.domain.repisitoryData.UseCase

import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import javax.inject.Inject

class UseCaseUpdateUserPosition @Inject constructor(
    private val repositoryData: RepositoryData
){
    suspend operator fun invoke(){
       return repositoryData.updateUserPosition()
    }
}

