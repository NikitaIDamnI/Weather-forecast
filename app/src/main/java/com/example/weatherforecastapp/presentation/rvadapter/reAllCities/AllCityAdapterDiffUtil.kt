package com.example.weatherforecastapp.presentation.rvadapter.reAllCities

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecastapp.domain.models.Location

class AllCityAdapterDiffUtil : DiffUtil.ItemCallback<Location>() {
    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.position == newItem.position
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }
}