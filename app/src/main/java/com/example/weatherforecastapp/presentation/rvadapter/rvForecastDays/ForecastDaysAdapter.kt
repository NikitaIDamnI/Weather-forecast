package com.example.weatherforecastapp.presentation.rvadapter.rvForecastDays

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherforecastapp.databinding.ItemForForecastDayBinding
import com.example.weatherforecastapp.domain.models.ForecastDay
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation
import com.squareup.picasso.Picasso

class ForecastDaysAdapter(
    private val context: Context
) : ListAdapter<ForecastDay, ForecastDaysViewHolder>(ForecastDaysDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastDaysViewHolder {
        val binding = ItemForForecastDayBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return ForecastDaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastDaysViewHolder, position: Int) {
        val forecastDays = getItem(position)
        with(holder.binding) {
            with(forecastDays) {
                if (position == USER_POSITION) {
                    tvTime.text = "Сегодня"
                }else{
                    tvTime.text = date
                }
                val maxTemp = days.maxtempC.toInt().toString() + WeatherPrecipitation.VALUE_DEGREE
                tvMaxTemp.text = maxTemp
                val minTemp = days.mintempC.toInt().toString() + WeatherPrecipitation.VALUE_DEGREE
                tvLowTemp.text = minTemp
                Picasso.get().load(days.condition.icon).into(imWeather)


            }

        }

    }

    companion object {
        const val USER_POSITION = 0
    }
}