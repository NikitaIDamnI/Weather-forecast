package com.example.weatherforecastapp.presentation.rvadapter.rvCurrentDay

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherforecastapp.databinding.ItemForCurrentDayBinding
import com.example.weatherforecastapp.domain.models.ForecastHour
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation
import com.squareup.picasso.Picasso

class CurrentAdapter(
    private val context: Context
) : ListAdapter<ForecastHour, CurrentViewHolder>(CurrentDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentViewHolder {
        val binding = ItemForCurrentDayBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return CurrentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrentViewHolder, position: Int) {
        val forecastHour = getItem(position)
        with(holder.binding) {
            with(forecastHour) {
                if (position == 0) {
                    tvTime.text = "Сейчас"
                } else {
                    tvTime.text = formatTime(time)
                }
                if (temp_c != ForecastHour.NOT_TEMP) {
                    val degree = temp_c.toString() + WeatherPrecipitation.VALUE_DEGREE
                    tvDegree.text = degree
                    Picasso.get().load(condition.icon).into(imWeather)
                } else {
                    tvDegree.text = condition.text
                    imWeather.setImageResource(condition.icon.toInt())
                }

            }

        }

    }

    private fun formatTime(time: String): String {
        try {
            val format = time.split(" ")
            return format[1]
        } catch (e: Exception) {
            return time
        }

    }


}