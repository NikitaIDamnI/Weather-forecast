package com.example.weatherforecastapp.presentation.rvadapter.reAllCities

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.example.weatherforecastapp.databinding.ItemFragmetCityBinding
import com.example.weatherforecastapp.databinding.ItemFragmetCityNotInternetBinding
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation

class AllCityAdapter(
    private val context: Context,
    private val internet: Boolean
) : ListAdapter<Location, AllCityAdapterViewHolder>(AllCityAdapterDiffUtil()) {

    var onClick: ((id: Int) -> Unit)? = null
    var subCondition: ((binding: ViewBinding) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCityAdapterViewHolder {
        val binding: ViewBinding
        if (internet) {
            binding = ItemFragmetCityBinding
                .inflate(LayoutInflater.from(context), parent, false)
        } else {
            binding = ItemFragmetCityNotInternetBinding
                .inflate(LayoutInflater.from(context), parent, false)
        }


        return AllCityAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllCityAdapterViewHolder, position: Int) {
        val city = getItem(position)

        when (val binding = holder.binding) {

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
                    root.setOnClickListener {
                        onClick?.invoke(position)
                    }
                    subCondition?.invoke(binding)
                }

            }

            else -> {
                with(binding as ItemFragmetCityNotInternetBinding) {
                    if (city.positionId == USER_POSITION) {
                        tvCity.text = "Текущее положение"
                        tvTime.text = city.name
                    } else {
                        tvCity.text = city.name
                        tvTime.text = city.localtime
                    }
                    tvMaxMin.text = formatTime(city.day_maxtempC.toInt(), city.day_mintempC.toInt())
                    val temp = "${city.temp_c.toInt()}${WeatherPrecipitation.VALUE_DEGREE}"
                    tvDegree.text = temp
                    root.setOnClickListener {
                        onClick?.invoke(position)
                    }
                    subCondition?.invoke(binding)
                }
            }
        }
    }

    private fun formatTime(maxTemp: Int, minTemp: Int): String {
        return "Макс.: ${maxTemp}º,Мин.: ${minTemp}º"
    }


    companion object {
        const val USER_POSITION = 0
    }
}