package com.example.weatherforecastapp.presentation.rvadapter.reAllCities

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.weatherforecastapp.databinding.ItemFragmetCityBinding
import com.example.weatherforecastapp.databinding.ItemFragmetCityNotInternetBinding
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation

class AllCityAdapterViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        city: Location,
        position: Int,
        onClick: ((id: Int) -> Unit)?,
    ) {
        when (binding) {
            is ItemFragmetCityBinding -> {
                with(binding) {
                    if (city.positionId == USER_POSITION) {
                        tvCity.text = "Текущее положение"
                        tvTime.text = city.name
                    } else {
                        tvCity.text = city.name
                        tvTime.text = city.localtime
                    }
                    tvCondition.text = city.condition_text
                    tvMaxMin.text = formatTime(city.day_maxtempC.toInt(), city.day_mintempC.toInt())
                    val temp = "${city.temp_c.toInt()}${WeatherPrecipitation.VALUE_DEGREE}"
                    tvDegree.text = temp
                    root.setOnClickListener { onClick?.invoke(position) }
                }
            }
            is ItemFragmetCityNotInternetBinding -> {
                with(binding) {
                    if (city.positionId == USER_POSITION) {
                        tvCity.text = "Текущее положение"
                        tvTime.text = city.name
                    } else {
                        tvCity.text = city.name
                    }
                    tvMaxMin.text = formatTime(city.day_maxtempC.toInt(), city.day_mintempC.toInt())
                    val temp = "${city.temp_c.toInt()}${WeatherPrecipitation.VALUE_DEGREE}"
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
        const val USER_POSITION = 0
    }
}



