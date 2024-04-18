package com.example.weatherforecastapp.presentation.rvadapter.rvSearchCity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherforecastapp.databinding.ItemSeachCityBinding
import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.presentation.rvadapter.reAllCities.SearchCityAdapterViewHolder
import com.example.weatherforecastapp.presentation.rvadapter.reAllCities.SearchCityDiffUtil

class SearchCityAdapter(
    private val context: Context
) : ListAdapter<SearchCity, SearchCityAdapterViewHolder>(SearchCityDiffUtil()) {

    var onClick: ((searchCity: SearchCity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCityAdapterViewHolder {
        val binding = ItemSeachCityBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return SearchCityAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchCityAdapterViewHolder, position: Int) {
        val searchCity = getItem(position)
        with(holder.binding) {
            with(searchCity) {
                val city = "$name $country $region"
                textView2.text = city
                root.setOnClickListener {
                    onClick?.invoke(searchCity)
                }
            }
        }

    }

}