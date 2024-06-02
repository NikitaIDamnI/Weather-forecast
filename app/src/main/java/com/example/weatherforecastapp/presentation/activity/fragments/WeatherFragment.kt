package com.example.weatherforecastapp.presentation.activity.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.databinding.WeatherFragmentBinding
import com.example.weatherforecastapp.domain.StateCity
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.rvadapter.rvCurrentDay.CurrentAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvForecastDays.ForecastDaysAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvPrecipitation.PrecipitationAdapter
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherFragment : Fragment() {

    private var _binding: WeatherFragmentBinding? = null
    private val binding: WeatherFragmentBinding
        get() = _binding ?: throw RuntimeException("WeatherFragmentBinding = null")

    private val args by navArgs<WeatherFragmentArgs>()

    private lateinit var viewModel: ViewModelWeather

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
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
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[ViewModelWeather::class.java]
        initCurrent()
        initForecast()
    }


    private fun initCurrent() {
        val adapterPrecipitation = PrecipitationAdapter(requireContext())
        with(binding) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.cities.collect { city ->
                        if (city is StateCity.Cities) {
                            val current = city.cities[args.position].current
                            Log.d(TAG, "initCurrent:$current ")

                            tvCity.text = current.nameCity
                            tvData.text = current.date
                            val temp =
                                current.temp_c.toInt()
                                    .toString() + WeatherPrecipitation.VALUE_DEGREE
                            tvDegree.text = temp
                            tvCondition.text = current.condition.text
                            Picasso.get().load(current.condition.icon).into(imWeather)
                            val listPrecipitation = current.weatherPrecipitation
                            adapterPrecipitation.submitList(listPrecipitation)
                            setupPullAdapter(rvPrecipitation)
                            rvPrecipitation.adapter = adapterPrecipitation
                            binding.imBackground.transitionName = "${id}"
                        }
                    }
                }
            }
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cities.collect { city ->
                    with(binding) {
                        if (city is StateCity.Cities) {
                            val forecastDay = city.cities[args.position].forecastDays
                            Log.d(TAG, "forecastDay:$forecastDay ")

                            val listWeatherHour = viewModel.getWeatherHour24(forecastDay)
                            adapterCurrent.submitList(listWeatherHour)
                            rvCurrentDay.adapter = adapterCurrent
                            adapterForecastDays.submitList(forecastDay.forecastDays)
                            rvForecastForDays.adapter = adapterForecastDays
                        }
                    }
                }
            }
        }
    }

    companion object{
        const val TAG = "WeatherFragment_Log"
    }
}