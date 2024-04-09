package com.example.weatherforecastapp.presentation.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.data.gps.LocationRepositoryImpl
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.models.Astro
import com.example.weatherforecastapp.domain.models.Condition
import com.example.weatherforecastapp.domain.models.Current
import com.example.weatherforecastapp.domain.models.ForecastDay
import com.example.weatherforecastapp.domain.models.ForecastHour
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseWeatherUpdate
import com.example.weatherforecastapp.domain.repositoryLocation.UseCase.UseCaseCheckLocation
import kotlinx.coroutines.launch


class ViewModelWeather(
    application: Application,
    val id: Int
) : AndroidViewModel(application) {

    private val repository = RepositoryDataImpl(application)
    private val repositoryLocal = LocationRepositoryImpl(application)
    private val useCaseCheckLocation = UseCaseCheckLocation(repositoryLocal)
    private val useCaseWeatherUpdate = UseCaseWeatherUpdate(repository)


    var location = repository.getLocation(id)
    var current = repository.getCurrentDay(id)
    var forecastDay = repository.getForecastDas(id)
    var sizeCity = repository.getSizePager()

    fun getWeatherPrecipitation(current: Current): List<WeatherPrecipitation> {
        return current.weatherPrecipitation
    }


    fun getWeatherHour24(forecastDay: List<ForecastDay>, astro: Astro): List<ForecastHour> {
        val sunriseHour = astro.sunrise.split(":")[0].toInt()
        val sunsetHour = astro.sunset.split(":")[0].toInt()

        val sunrise = ForecastHour(
            time = astro.sunrise,
            condition = Condition(
                text = "Sunset",
                icon = R.drawable.sunset_weather.toString()
            )
        )
        val sunset = ForecastHour(
            time = astro.sunset,
            condition = Condition(
                text = "Sunset",
                icon = R.drawable.sunset_weather.toString()
            )
        )

        Log.d("ViewModelWeather_Log", "sunrise: ${sunrise.time}")
        Log.d("ViewModelWeather_Log", "sunset: ${sunset.time}")


        val startIndex = forecastDay[0].timeLocation.split(":")[0].toInt()
        val oldListTo24 = forecastDay[0].forecastHour
        val oldListNextDay = forecastDay[1].forecastHour
        val newListTo24 = oldListTo24.slice(startIndex until oldListTo24.size)
        val newListNextDay = oldListNextDay.slice(0 until startIndex + 1)
        return mutableListOf<ForecastHour>().apply {
            addAll(newListTo24)
            addAll(newListNextDay)
        }
    }


    fun weatherUpdate() {
        viewModelScope.launch {
            useCaseWeatherUpdate.invoke()
        }
    }


    fun checkLocation(context: Context) {
        useCaseCheckLocation.invoke(context)
    }


}
