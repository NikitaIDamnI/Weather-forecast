package com.example.weatherforecastapp.presentation.rvadapter.reAllCities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.example.weatherforecastapp.databinding.ItemFragmetCityBinding
import com.example.weatherforecastapp.databinding.ItemFragmetCityNotInternetBinding
import com.example.weatherforecastapp.domain.models.Location

class AllCityAdapter(
    private var internet: Boolean = false
) : ListAdapter<Location, AllCityAdapterViewHolder>(AllCityAdapterDiffUtil()) {

    fun updateInternetState(newInternetState: Boolean) {
        if (this.internet != newInternetState) {
            this.internet = newInternetState
            notifyDataSetChanged()
        }
    }

    var onClick: ((id: Int) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return if (internet) VIEW_TYPE_INTERNET else VIEW_TYPE_NO_INTERNET
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCityAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewBinding = when (viewType) {
            VIEW_TYPE_INTERNET -> ItemFragmetCityBinding.inflate(layoutInflater, parent, false)
            VIEW_TYPE_NO_INTERNET -> ItemFragmetCityNotInternetBinding.inflate(layoutInflater, parent, false)
            else -> throw IllegalArgumentException("Invalid view type")
        }
        return AllCityAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllCityAdapterViewHolder, position: Int) {
        val city = getItem(position)
        holder.bind(city, position, onClick)
    }

    companion object {
        const val VIEW_TYPE_INTERNET = 1
        const val VIEW_TYPE_NO_INTERNET = 2
    }
}