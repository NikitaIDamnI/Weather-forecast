package com.example.weatherforecastapp.presentation.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.data.gps.LocationRepositoryImpl
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.models.Current
import com.example.weatherforecastapp.domain.models.ForecastDay
import com.example.weatherforecastapp.domain.models.ForecastHour
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCasNumberOfCities
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseWeatherUpdate
import com.example.weatherforecastapp.domain.repositoryLocation.LocationRepository
import com.example.weatherforecastapp.domain.repositoryLocation.UseCase.UseCaseCheckLocation
import com.example.weatherforecastapp.domain.repositoryLocation.UseCase.UseCaseUpdateDataLocation
import kotlinx.coroutines.launch


class ViewModelWeather(
    application: Application,
    val id: Int
) : AndroidViewModel(application) {

    private val repository = RepositoryDataImpl(application)
    private val repositoryLocal = LocationRepositoryImpl(application)
    private val useCaseCheckLocation = UseCaseCheckLocation(repositoryLocal)
    private val useCasNumberOfCities = UseCasNumberOfCities(repository)
    private val useCaseWeatherUpdate = UseCaseWeatherUpdate(repository)
    private val setUpdateListener = UseCaseUpdateDataLocation(repositoryLocal)


    var location = repository.getLocation(id)
    var current = repository.getCurrentDay(id)
    var forecastDay = repository.getForecastDas(id)
    var sizeCity = MutableLiveData<Int>()

    init {
        numberOfCities()
    }

    fun updateData() {
        setUpdateListener(update())
    }

    private fun update() = object : LocationRepository.LocationUpdateListener {
        override fun onUpdate() {
            location = repository.getLocation(id)
            current = repository.getCurrentDay(id)
            forecastDay = repository.getForecastDas(id)
            numberOfCities()
        }
    }


    fun getWeatherPrecipitation(current: Current): List<WeatherPrecipitation> {
        return current.weatherPrecipitation
        /* current.weatherPrecipitation.filter {
                    (it.unit == WeatherPrecipitation.VALUE_DEGREE)
                            || (it.unit != WeatherPrecipitation.VALUE_PERCENT && it.value > 0)

         */
    }


fun getWeatherHour24(forecastDay: List<ForecastDay>): List<ForecastHour> {
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

private fun numberOfCities() {
    viewModelScope.launch {
        sizeCity.value = useCasNumberOfCities.invoke()
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
