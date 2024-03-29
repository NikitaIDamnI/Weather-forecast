package com.example.weatherforecastapp.presentation.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.databinding.WeatherFragmentBinding
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation
import com.example.weatherforecastapp.presentation.rvadapter.rvCurrentDay.CurrentAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvForecastDays.ForecastDaysAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvPrecipitation.PrecipitationAdapter
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import com.squareup.picasso.Picasso

class WeatherFragment : Fragment() {

    private var _binding: WeatherFragmentBinding? = null
    private val binding: WeatherFragmentBinding
        get() = _binding ?: throw RuntimeException("WeatherFragmentBinding = null")

    private val viewModelFactory by lazy {
        ViewModelFactory(requireActivity().application, parseArgument())
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ViewModelWeather::class.java]
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
        requireActivity().supportStartPostponedEnterTransition()
        postponeEnterTransition()
        initCurrent()
        initForecast()
        requireActivity().supportStartPostponedEnterTransition()
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
                Log.d("WeatherFragment", "weatherPrecipitation: ${it.weatherPrecipitation}")
                val listPrecipitation = viewModel.getWeatherPrecipitation(it)
                adapterPrecipitation.submitList(listPrecipitation)
                rvPrecipitation.adapter = adapterPrecipitation
                binding.imBackground.transitionName =  "${parseArgument()}"
            })

        }
    }

    private fun initForecast() {
        val adapterCurrent = CurrentAdapter(requireContext())
        val adapterForecastDays = ForecastDaysAdapter(requireContext())
        viewModel.forecastDay.observe(viewLifecycleOwner, Observer {
            with(binding) {
                val listWeatherHour = viewModel.getWeatherHour(it[0])
                adapterCurrent.submitList(listWeatherHour)
                rvCurrentDay.adapter = adapterCurrent
                adapterForecastDays.submitList(it)
                rvForecastForDays.adapter = adapterForecastDays
            }
        })
    }

    override fun onResume() {
        super.onResume()
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