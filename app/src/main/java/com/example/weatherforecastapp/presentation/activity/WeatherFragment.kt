package com.example.weatherforecastapp.presentation.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.databinding.WeatherFragmentBinding
import com.example.weatherforecastapp.presentation.rvadapter.rvCurrentDay.CurrentAdapter
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import com.squareup.picasso.Picasso

class WeatherFragment : Fragment() {

    private var _binding: WeatherFragmentBinding? = null
    private val binding: WeatherFragmentBinding
        get() = _binding ?: throw RuntimeException("WeatherFragmentBinding = null")

    private val viewModel by lazy {
        ViewModelProvider(this)[ViewModelWeather::class.java]
    }

    override fun onAttach(context: Context) {
        viewModel.getCity(parseArgument())
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCurrent()
        initForecast()
    }


    private fun initCurrent() {
        with(binding) {
            viewModel.current.observe(viewLifecycleOwner, Observer {

                tvCity.text = it.nameCity
                tvData.text = it.date
                tvDegree.text = it.temp_c.toInt().toString()
                tvCondition.text = it.condition.text
                Picasso.get().load(it.condition.icon).into(imWeather)

            })

        }
    }

    private fun initForecast() {
        val adapterCurrent = CurrentAdapter(requireContext())
        viewModel.forecastDay.observe(viewLifecycleOwner, Observer {
            with(binding) {
                val list = viewModel.getWeatherHour(it[0])
                adapterCurrent.submitList(list)
                rvCurrentDay.adapter = adapterCurrent
            }
        })
    }


    private fun parseArgument(): Int {
        return requireArguments().getInt(KEY_ID_CITY)
    }

    companion object {
        private const val KEY_ID_CITY = "id city"
        fun newInstance(idCity: Int): WeatherFragment {
            return WeatherFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_ID_CITY, idCity)
                }
            }
        }
    }
}