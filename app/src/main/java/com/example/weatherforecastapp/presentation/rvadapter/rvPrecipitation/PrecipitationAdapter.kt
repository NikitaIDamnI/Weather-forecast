package com.example.weatherforecastapp.presentation.rvadapter.rvPrecipitation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherforecastapp.databinding.ItemPrecipitationBinding
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation

class PrecipitationAdapter(
    private val context: Context
) : ListAdapter<WeatherPrecipitation, PrecipitationViewHolder>(PrecipitationDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrecipitationViewHolder {
        val binding = ItemPrecipitationBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return PrecipitationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrecipitationViewHolder, position: Int) {
        val forecastHour = getItem(position)
        with(holder.binding) {
            with(forecastHour) {
                tvPrecipitation
                tvUnit
                tvValue
            }

        }

    }

    private fun formatTime(time: String):String{
        val format = time.split(" ")
        return format[1]
    }
}