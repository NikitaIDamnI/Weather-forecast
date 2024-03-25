package com.example.weatherforecastapp.presentation.rvadapter.reAllCities

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecastapp.domain.models.SearchCity

class SearchCityDiffUtil() : DiffUtil.ItemCallback<SearchCity>() {

    override fun areItemsTheSame(oldItem: SearchCity, newItem: SearchCity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SearchCity, newItem: SearchCity): Boolean {
        return oldItem == newItem
    }

}