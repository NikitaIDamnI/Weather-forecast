package com.example.weatherforecastapp.data.dagger

import android.app.Application
import com.example.weatherforecastapp.ActivityPreview
import com.example.weatherforecastapp.WeatherFragment
import com.example.weatherforecastapp.data.dagger.module.ModuleData
import com.example.weatherforecastapp.data.dagger.module.ModuleLocation
import com.example.weatherforecastapp.data.dagger.subcomponent.ActivityWeatherSubComp
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        ModuleData::class,
        ModuleLocation::class
    ]
)
interface AppComponent {

    fun inject(activityPreview: ActivityPreview)
    fun activityWeatherInject(): ActivityWeatherSubComp.Factory

    fun inject(fragment: WeatherFragment)

    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance application: Application

        ):AppComponent
    }

}