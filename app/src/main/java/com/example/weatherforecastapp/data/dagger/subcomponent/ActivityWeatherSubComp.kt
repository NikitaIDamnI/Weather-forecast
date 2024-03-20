package com.example.weatherforecastapp.data.dagger.subcomponent

import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecastapp.presentation.activity.ActivityWeather
import com.example.weatherforecastapp.data.dagger.module.ModuleData
import com.example.weatherforecastapp.data.dagger.module.ModuleLocation
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        ModuleData::class,
        ModuleLocation::class
    ]
)
interface ActivityWeatherSubComp {
    fun inject(activityWeather: ActivityWeather)

    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance appActivity: AppCompatActivity,
            @BindsInstance id: Int
        ): ActivityWeatherSubComp
    }

}

