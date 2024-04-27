package com.example.weatherforecastapp.presentation.rvadapter.reAllCities

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherforecastapp.databinding.ItemFragmetCityBinding
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation

class AllCityAdapter(
    private val context: Context,
) : ListAdapter<Location, AllCityAdapterViewHolder>(AllCityAdapterDiffUtil()) {

    var onClick: ((id: Int, binding: ItemFragmetCityBinding) -> Unit)? = null
    var subCondition: ((binding: ItemFragmetCityBinding) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCityAdapterViewHolder {
        val binding = ItemFragmetCityBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return AllCityAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllCityAdapterViewHolder, position: Int) {
        val city = getItem(position)
        with(holder.binding) {
            with(city) {

                if (city.positionId == USER_POSITION) {
                    tvCity.text = "Текущее положение"
                    tvTime.text = name
                } else {
                    tvCity.text = name
                    tvTime.text = localtime
                }
                tvCondition.text = condition_text
                tvMaxMin.text = formatTime(day_maxtempC.toInt(), day_mintempC.toInt())
                val temp = temp_c.toInt().toString() + WeatherPrecipitation.VALUE_DEGREE
                tvDegree.text = temp
                root.setOnClickListener {
                    onClick?.invoke(position, holder.binding)
                }
                subCondition?.invoke(holder.binding)

            }


        }

    }

    private fun formatTime(maxTemp: Int, minTemp: Int): String {
        return "Макс.: ${maxTemp}º,Мин.: ${minTemp}º"
    }


    companion object {
        const val USER_POSITION = 0
    }
}