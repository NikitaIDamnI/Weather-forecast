package com.example.weatherforecastapp.data.dagger.module

import android.app.Application
import com.example.weatherforecastapp.data.dagger.ApplicationScope
import com.example.weatherforecastapp.data.gps.LocationRepositoryImpl
import com.example.weatherforecastapp.domain.repositoryLocation.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface ModuleLocation {
    @Binds
    fun bindLocationRepository(impl: LocationRepositoryImpl):LocationRepository
    companion object {
        @ApplicationScope
        @Provides
        fun provideFusedLocationProviderClient(appActivity: Application) : FusedLocationProviderClient {
           return LocationServices.getFusedLocationProviderClient(appActivity)
        }
    }
}