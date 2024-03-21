package com.example.weatherforecastapp.presentation.rvadapter.rvPrecipitation

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation

class PrecipitationDiffUtil : DiffUtil.ItemCallback<WeatherPrecipitation>() {


    override fun areItemsTheSame(
        oldItem: WeatherPrecipitation,
        newItem: WeatherPrecipitation
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: WeatherPrecipitation,
        newItem: WeatherPrecipitation
    ): Boolean {
        return oldItem == newItem
    }
}