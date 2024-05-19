package com.example.weatherforecastapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.models.Condition
import com.example.weatherforecastapp.domain.models.ForecastDaysCity
import com.example.weatherforecastapp.domain.models.ForecastHour
import javax.inject.Inject


class ViewModelWeather @Inject constructor(
    private val repositoryDataImpl: RepositoryDataImpl,
) : ViewModel() {

    val city = repositoryDataImpl.getCityLiveData(viewModelScope)


    fun getWeatherHour24(forecastDaysCity: ForecastDaysCity): List<ForecastHour> {
        val astro = forecastDaysCity.forecastDays[0].astro

        val sunriseHour = astro.sunrise.split(":")[0].toInt()
        val sunsetHour = astro.sunset.split(":")[0].toInt()

        val sunrise = ForecastHour(
            time = astro.sunrise,
            condition = Condition(
                text = "Sunrise",
                icon = R.drawable.sunrise_weather.toString()
            )
        )
        val sunset = ForecastHour(
            time = astro.sunset,
            condition = Condition(
                text = "Sunset",
                icon = R.drawable.sunset_weather.toString()
            )
        )
        val startIndex = forecastDaysCity.timeLocation.split(":")[0].toInt()
        val oldListTo24 = forecastDaysCity.forecastDays[0].forecastHour
        val oldListNextDay = forecastDaysCity.forecastDays[1].forecastHour

        val newListTo24 = oldListTo24.subList(startIndex, oldListTo24.size).toMutableList()
        val newListNextDay = oldListNextDay.subList(0, startIndex + 1).toMutableList()

        if (sunriseHour >= startIndex) {
            val index = sunriseHour - startIndex
            newListTo24.add(index + 1, sunrise)
        }
        if (sunsetHour >= startIndex) {
            val index = sunsetHour - startIndex
            newListTo24.add(index + 1, sunset)
        }
        if (sunriseHour < newListNextDay.size) {
            val index = sunsetHour - newListNextDay.size
            newListNextDay.add(sunriseHour + 1, sunrise)
        }
        val weatherHour24 = mutableListOf<ForecastHour>().apply {
            addAll(newListTo24)
            addAll(newListNextDay)
        }

        return weatherHour24
    }






}
