package com.example.weatherforecastapp.presentation.rvadapter.reAllCities

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.weatherforecastapp.databinding.ItemFragmetCityBinding
import com.example.weatherforecastapp.databinding.ItemFragmetCityNotInternetBinding
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation

class AllCityAdapterViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        location: Location,
        position: Int,
        onClick: ((id: Int) -> Unit)?,
    ) {
        when (binding) {
            is ItemFragmetCityBinding -> {
                with(binding) {
                    if (location.locationId == USER_LOCATION_ID) {
                        tvCity.text = "Текущее положение"
                        tvTime.text = location.name
                    } else {
                        tvCity.text = location.name
                        tvTime.text = location.localtime
                    }
                    tvCondition.text = location.condition_text
                    tvMaxMin.text = formatTime(location.day_maxtempC.toInt(), location.day_mintempC.toInt())
                    val temp = "${location.temp_c.toInt()}${WeatherPrecipitation.VALUE_DEGREE}"
                    tvDegree.text = temp
                    root.setOnClickListener { onClick?.invoke(position) }
                }
            }
            is ItemFragmetCityNotInternetBinding -> {
                with(binding) {
                    if (location.positionId == USER_LOCATION_ID) {
                        tvCity.text = "Текущее положение"
                        tvTime.text = location.name
                    } else {
                        tvCity.text = location.name
                    }
                    tvMaxMin.text = formatTime(location.day_maxtempC.toInt(), location.day_mintempC.toInt())
                    val temp = "${location.temp_c.toInt()}${WeatherPrecipitation.VALUE_DEGREE}"
                    tvDegree.text = temp
                    root.setOnClickListener { onClick?.invoke(position) }
                }
            }
        }
    }

    private fun formatTime(maxTemp: Int, minTemp: Int): String {
        return "Макс.: ${maxTemp}º,Мин.: ${minTemp}º"
    }

    companion object{
        const val USER_LOCATION_ID = 0
    }
}



