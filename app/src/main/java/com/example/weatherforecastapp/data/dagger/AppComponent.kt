package com.example.weatherforecastapp.data.dagger

import android.app.Application
import com.example.weatherforecastapp.data.dagger.module.ModuleData
import com.example.weatherforecastapp.data.dagger.module.ModuleLocation
import com.example.weatherforecastapp.data.dagger.module.ViewModelModule
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.activity.ActivityWeather
import com.example.weatherforecastapp.presentation.activity.fragments.FragmentAllCities
import com.example.weatherforecastapp.presentation.activity.fragments.FragmentPagerWeather
import com.example.weatherforecastapp.presentation.activity.fragments.PreviewNewWeatherFragment
import com.example.weatherforecastapp.presentation.activity.fragments.WeatherFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        ModuleData::class,
        ModuleLocation::class,
        ViewModelModule::class]
)
interface AppComponent {

    fun inject(activity: ActivityWeather)
    fun inject(fragment: WeatherFragment)
    fun inject(fragment: FragmentAllCities)
    fun inject(fragment: PreviewNewWeatherFragment)
    fun inject(fragment: FragmentPagerWeather)
    fun inject(application: WeatherApp)


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
        ): AppComponent
    }
}


