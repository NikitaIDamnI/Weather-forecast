package com.example.weatherforecastapp.presentation.rvadapter.rvPrecipitation

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.databinding.ItemPrecipitationBinding
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation

class PrecipitationAdapter(
    private val context: Context
) : ListAdapter<WeatherPrecipitation, PrecipitationViewHolder>(PrecipitationDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrecipitationViewHolder {
        when (viewType) {
            ENABLE-> R.layout.item_precipitation
        }
        val binding = ItemPrecipitationBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return PrecipitationViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: PrecipitationViewHolder, position: Int) {
        val precipitation = getItem(position)
        with(holder.binding) {
            with(precipitation) {
                when (unit) {
                    WeatherPrecipitation.VALUE_PERCENT -> {
                        tvPrecipitation.text = name
                        val valuePercentage = value.toString() + unit
                        tvValue.text = valuePercentage
                        tvUnit.visibility = View.GONE
                        progressBar.progress = value
                        progressBar.progressTintList = ColorStateList.valueOf(color)

                    }

                    WeatherPrecipitation.VALUE_DEGREE -> {
                        tvPrecipitation.text = name
                        val valueDegree = value.toString() + unit
                        tvValue.text = valueDegree
                        tvUnit.visibility = View.GONE
                        progressBar.progress = valuePercent
                        progressBar.progressTintList = ColorStateList.valueOf(color)


                    }

                    else -> {
                        tvPrecipitation.text = name
                        val valueDegree = value.toString()
                        tvValue.text = valueDegree
                        tvUnit.text = unit
                        tvUnit.visibility = View.VISIBLE
                        progressBar.progress = valuePercent
                        progressBar.progressTintList = ColorStateList.valueOf(color)


                    }
                }

            }


        }

    }

    companion object {
        const val ENABLE = 0
        const val MAX_PULL_SIZE = 8
    }


}