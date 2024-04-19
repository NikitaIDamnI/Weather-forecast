package com.example.weatherforecastapp.presentation

import android.app.Application
import com.example.weatherforecastapp.data.dagger.DaggerAppComponent

class WeatherApp : Application() {
    val component by lazy{
        DaggerAppComponent.factory()
            .create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }
}