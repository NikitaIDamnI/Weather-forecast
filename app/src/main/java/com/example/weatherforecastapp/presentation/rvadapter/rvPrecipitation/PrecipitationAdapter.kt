package com.example.weatherforecastapp.presentation.rvadapter.rvPrecipitation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
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
        val precipitation = getItem(position)
        with(holder.binding) {
            with(precipitation) {
                when (unit) {
                    WeatherPrecipitation.VALUE_PERCENT -> {
                        tvPrecipitation.text = name
                        val valuePercentage = value.toInt().toString() + unit
                        tvValue.text = valuePercentage
                        tvUnit.visibility = View.GONE
                        progressBar.progress = precipitation.value.toInt()
                    }

                    WeatherPrecipitation.VALUE_DEGREE -> {
                        tvPrecipitation.text = name
                        val valueDegree = value.toInt().toString() + unit
                        tvValue.text = valueDegree
                        tvUnit.visibility = View.GONE
                        progressBar.visibility = View.GONE
                    }

                    else -> {
                            tvPrecipitation.text = name
                            val valueDegree = value.toInt().toString()
                            tvValue.text = valueDegree
                            tvUnit.text = unit
                            tvUnit.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                    }
                }

            }


        }

    }


    private fun formatTime(time: String): String {
        val format = time.split(" ")
        return format[1]
    }
}