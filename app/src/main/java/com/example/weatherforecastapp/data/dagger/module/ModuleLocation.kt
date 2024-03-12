package com.example.weatherforecastapp.data.dagger.module

import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecastapp.data.gps.LocationClientImpl
import com.example.weatherforecastapp.domain.repositoryLocation.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface ModuleLocation {

    @Binds
    fun bindLocationRepository(impl: LocationClientImpl):LocationRepository
    companion object {
        @Provides
        fun provideFusedLocationProviderClient(appActivity: AppCompatActivity) : FusedLocationProviderClient {
           return LocationServices.getFusedLocationProviderClient(appActivity)
        }
    }
}