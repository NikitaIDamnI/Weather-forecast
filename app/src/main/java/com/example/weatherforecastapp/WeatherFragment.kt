package com.example.weatherforecastapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherforecastapp.databinding.WeatherFragmentBinding

class WeatherFragment : Fragment() {

    private var _binding: WeatherFragmentBinding? = null
    private val binding: WeatherFragmentBinding
        get() = _binding ?: throw RuntimeException("WeatherFragmentBinding = null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
        private const val KEY_ID_CITY = "id city"
        fun newInstance (idCity: Int) :WeatherFragment{
            return WeatherFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_ID_CITY,idCity)
                }
            }
        }
    }
}