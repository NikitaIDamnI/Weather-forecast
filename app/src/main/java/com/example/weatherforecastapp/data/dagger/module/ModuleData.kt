package com.example.weatherforecastapp.data.dagger.module

import android.app.Application
import com.example.testapi.network.ApiFactory
import com.example.testapi.network.ApiService
import com.example.weatherforecastapp.data.dagger.ApplicationScope
import com.example.weatherforecastapp.data.database.AppDatabase
import com.example.weatherforecastapp.data.database.dao.CurrentDao
import com.example.weatherforecastapp.data.database.dao.ForecastDayDao
import com.example.weatherforecastapp.data.database.dao.LocationDao
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.repisitoryData.RepositoryData
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface ModuleData {
    @Binds
    fun provideRepository(impl: RepositoryDataImpl): RepositoryData

    companion object {
        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
        @ApplicationScope
        @Provides
        fun provideCurrentDao(application: Application): CurrentDao {
            return AppDatabase.getInstance(application).currentDao()
        }
        @ApplicationScope
        @Provides
        fun provideForecastDayDao(application: Application): ForecastDayDao {
            return AppDatabase.getInstance(application).forecastDayDao()
        }
        @ApplicationScope
        @Provides
        fun provideLocationDao(application: Application): LocationDao {
            return AppDatabase.getInstance(application).locationDao()
        }
    }
}