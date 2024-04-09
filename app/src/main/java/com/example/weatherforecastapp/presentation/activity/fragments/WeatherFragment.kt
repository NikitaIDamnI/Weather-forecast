package com.example.weatherforecastapp.presentation.activity.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.databinding.WeatherFragmentBinding
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation
import com.example.weatherforecastapp.presentation.rvadapter.rvCurrentDay.CurrentAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvForecastDays.ForecastDaysAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvPrecipitation.PrecipitationAdapter
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactoryWeather
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import com.squareup.picasso.Picasso

class WeatherFragment : Fragment() {

    private var _binding: WeatherFragmentBinding? = null
    private val binding: WeatherFragmentBinding
        get() = _binding ?: throw RuntimeException("WeatherFragmentBinding = null")

    private val args by navArgs<WeatherFragmentArgs>()


    private val viewModelFactoryWeather by lazy {
        ViewModelFactoryWeather(requireActivity().application, args.id)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactoryWeather)[ViewModelWeather::class.java]
    }


    override fun onAttach(context: Context) {
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
        val adapterPrecipitation = PrecipitationAdapter(requireContext())
        with(binding) {
            viewModel.current.observe(viewLifecycleOwner, Observer {
                tvCity.text = it.nameCity
                tvData.text = it.date
                val temp = it.temp_c.toInt().toString() + WeatherPrecipitation.VALUE_DEGREE
                tvDegree.text = temp
                tvCondition.text = it.condition.text
                Picasso.get().load(it.condition.icon).into(imWeather)
                val listPrecipitation = viewModel.getWeatherPrecipitation(it)
                Log.d("WeatherFragment_Log", "initCurrent: $listPrecipitation")
                adapterPrecipitation.submitList(listPrecipitation)
                setupPullAdapter(rvPrecipitation)
                rvPrecipitation.adapter = adapterPrecipitation
                binding.imBackground.transitionName = "${args.id}"
            })

        }
    }

    private fun setupPullAdapter(rvPrecipitation: RecyclerView) {

        rvPrecipitation.recycledViewPool.setMaxRecycledViews(
            PrecipitationAdapter.ENABLE,
            PrecipitationAdapter.MAX_PULL_SIZE
        )



    }

    private fun initForecast() {
        val adapterCurrent = CurrentAdapter(requireContext())
        val adapterForecastDays = ForecastDaysAdapter(requireContext())
        viewModel.forecastDay.observe(viewLifecycleOwner, Observer {
            with(binding) {
                val listWeatherHour = viewModel.getWeatherHour24(it,it[0].astro)
                adapterCurrent.submitList(listWeatherHour)
                rvCurrentDay.adapter = adapterCurrent
                adapterForecastDays.submitList(it)
                rvForecastForDays.adapter = adapterForecastDays
            }
        })
    }


}