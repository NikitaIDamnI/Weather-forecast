package com.example.weatherforecastapp.presentation.rvadapter.reAllCities

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.example.weatherforecastapp.databinding.ItemFragmetCityBinding
import com.example.weatherforecastapp.databinding.ItemFragmetCityNotInternetBinding
import com.example.weatherforecastapp.domain.models.Location

class AllCityAdapter(
    private val context: Context,
    private val internet: Boolean
) : ListAdapter<Location, AllCityAdapterViewHolder>(AllCityAdapterDiffUtil()) {

    var onClick: ((id: Int) -> Unit)? = null

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
        holder.bind(city,position,onClick)
    }
}