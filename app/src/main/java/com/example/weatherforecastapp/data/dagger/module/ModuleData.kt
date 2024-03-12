package com.example.weatherforecastapp.data.dagger.module

import com.example.testapi.network.ApiFactory
import com.example.testapi.network.ApiService
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface ModuleData {


    @Binds
    fun provideRepository(impl: RepositoryDataImpl) : RepositoryData

    companion object{

        @Provides
        fun provideApiService(): ApiService{
           return ApiFactory.apiService
        }

    }

}