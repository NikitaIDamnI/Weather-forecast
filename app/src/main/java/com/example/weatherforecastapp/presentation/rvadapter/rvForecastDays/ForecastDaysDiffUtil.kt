package com.example.weatherforecastapp.presentation.rvadapter.rvForecastDays

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecastapp.domain.models.ForecastDay

class ForecastDaysDiffUtil : DiffUtil.ItemCallback<ForecastDay>() {
    override fun areItemsTheSame(oldItem: ForecastDay, newItem: ForecastDay): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: ForecastDay, newItem: ForecastDay): Boolean {
        return oldItem == newItem
    }
}