package com.example.weatherforecastapp.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.data.gps.LocationRepositoryImpl
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.models.Current
import com.example.weatherforecastapp.domain.models.ForecastDay
import com.example.weatherforecastapp.domain.models.ForecastHour
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCasNumberOfCities
import com.example.weatherforecastapp.domain.repositoryLocation.UseCase.UseCaseCheckLocation
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Timer
import java.util.TimerTask


class ViewModelWeather(application: Application) : AndroidViewModel(application) {

    private val repository = RepositoryDataImpl(application)
    private val repositoryLocal = LocationRepositoryImpl(application)
    private val useCaseCheckLocation = UseCaseCheckLocation(repositoryLocal)
    private val useCasNumberOfCities = UseCasNumberOfCities(repository)

    var time = MutableLiveData<String>()
    private var localTime: String? = ""
    val location = MutableLiveData<Location>()
    val current = MutableLiveData<Current>()
    val forecastDay = MutableLiveData<List<ForecastDay>>()
    var sizeCity = MutableLiveData<Int>(MIN_SIZE_CITY)

    init {
        numberOfCities()
    }

    fun getCity(cityId: Int) {
        repository.gerLocation(cityId).observeForever {
            location.value = it
        }
        repository.getCurrentDay(cityId).observeForever {
            current.value = it
        }
        repository.forecastDas(cityId).observeForever {
            forecastDay.value = it
        }
    }

    fun getWeatherPrecipitation(current: Current): List<WeatherPrecipitation> {
        return current.weatherPrecipitation.filter {
            (it.unit == WeatherPrecipitation.VALUE_DEGREE)
                    || (it.unit != WeatherPrecipitation.VALUE_PERCENT && it.value > 0)
        }
    }

    fun getWeatherHour(forecastDay: ForecastDay): List<ForecastHour> {
        val startIndex = forecastDay.timeLocation.split(":")[0].toInt()
        val list = forecastDay.forecastHour
        return list.slice(startIndex until list.size)
    }

    private fun numberOfCities() {
        viewModelScope.launch {
            sizeCity.value = useCasNumberOfCities.invoke()
        }

    }


    private fun startTime(timeLocation: String) {
        val timer = Timer()

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (localTime == "") {
                    val currentTimeMiles = System.currentTimeMillis()
                    time.postValue(formatTime(currentTimeMiles))
                } else {

                }

            }
        }, 0, 10000)
    }


    fun checkLocation() {
        useCaseCheckLocation.invoke()
    }

    private fun formatTime(timeEpochMillis: Long): String {
        val instant = Instant.ofEpochMilli(timeEpochMillis)
        val formatter = DateTimeFormatter.ofPattern("HH:mm").withLocale(Locale.ENGLISH)
        return formatter.format(instant.atZone(ZoneId.systemDefault()))
    }


    companion object {
        const val MIN_SIZE_CITY = 1
    }
}