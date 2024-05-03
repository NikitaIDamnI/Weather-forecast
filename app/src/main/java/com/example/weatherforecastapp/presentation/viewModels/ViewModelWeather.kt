package com.example.weatherforecastapp.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.domain.models.Condition
import com.example.weatherforecastapp.domain.models.Current
import com.example.weatherforecastapp.domain.models.ForecastDayCity
import com.example.weatherforecastapp.domain.models.ForecastHour
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseGetCurrents
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseGetForecastDaysCity
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseGetLocations
import javax.inject.Inject


class ViewModelWeather @Inject constructor(
    private val getLocations: UseCaseGetLocations,
    private val getCurrentDays: UseCaseGetCurrents,
    private val getForecastDaysCity: UseCaseGetForecastDaysCity,
) : ViewModel() {

    var location = getLocations()
    var current = getCurrentDays()
    var forecastDay = getForecastDaysCity()

    val locationCondition =
        MutableLiveData<Boolean>(true)

    fun checkLocationCondition(condition: Boolean){
        locationCondition.value = condition
    }

    fun getWeatherPrecipitation(current: Current): List<WeatherPrecipitation> {
        return current.weatherPrecipitation
    }
    fun getWeatherHour24(forecastDayCity: ForecastDayCity): List<ForecastHour> {
        val astro = forecastDayCity.forecastDays[0].astro

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
        val startIndex = forecastDayCity.timeLocation.split(":")[0].toInt()
        val oldListTo24 = forecastDayCity.forecastDays[0].forecastHour
        val oldListNextDay = forecastDayCity.forecastDays[1].forecastHour

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
