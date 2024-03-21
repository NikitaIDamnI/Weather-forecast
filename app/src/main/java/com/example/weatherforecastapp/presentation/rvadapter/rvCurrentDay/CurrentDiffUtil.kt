package com.example.weatherforecastapp.presentation.rvadapter.rvCurrentDay

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecastapp.domain.models.ForecastHour

class CurrentDiffUtil : DiffUtil.ItemCallback<ForecastHour>() {
    override fun areItemsTheSame(oldItem: ForecastHour, newItem: ForecastHour): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(oldItem: ForecastHour, newItem: ForecastHour): Boolean {
        return oldItem == newItem
    }
}