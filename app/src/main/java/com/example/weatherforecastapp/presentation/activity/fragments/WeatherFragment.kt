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
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.rvadapter.rvCurrentDay.CurrentAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvForecastDays.ForecastDaysAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvPrecipitation.PrecipitationAdapter
import com.example.weatherforecastapp.presentation.viewModels.ViewModelAllCities
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelNetworkStatus
import com.squareup.picasso.Picasso
import javax.inject.Inject

class WeatherFragment : Fragment() {

    private var _binding: WeatherFragmentBinding? = null
    private val binding: WeatherFragmentBinding
        get() = _binding ?: throw RuntimeException("WeatherFragmentBinding = null")

    private val args by navArgs<WeatherFragmentArgs>()

    private lateinit var viewModelNetworkStatus: ViewModelNetworkStatus
    private lateinit var viewModel: ViewModelAllCities

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
            ViewModelProvider(requireActivity(), viewModelFactory)[ViewModelAllCities::class.java]
        viewModelNetworkStatus =
            ViewModelProvider(
                requireActivity(),
                viewModelFactory
            )[ViewModelNetworkStatus::class.java]
        initCurrent()
        initForecast()
        initLocationCheck()
        viewModel.city.observe(viewLifecycleOwner) { city ->
            if (checkData(city.size)) {
                Log.d("WeatherFragment_Log", "city: ${city.get(args.position).forecastDays.forecastDays}")
            }

        }

    }


    private fun initCurrent() {
        val adapterPrecipitation = PrecipitationAdapter(requireContext())
        with(binding) {
            viewModel.city.observe(viewLifecycleOwner, Observer { city ->
                if (checkData(city.size)) {
                    val current = city[args.position].current
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
            })
        }

    }

    private fun initLocationCheck() {
        viewModelNetworkStatus.state.observe(viewLifecycleOwner) { state ->
            if (state.location) {
                binding.imNotLocation.visibility = View.GONE
            } else {
                viewModel.city.observe(viewLifecycleOwner) { city ->
                    if (checkData(city.size)) {
                        binding.imNotLocation.visibility = View.VISIBLE
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
        viewModel.city.observe(viewLifecycleOwner, Observer {
            with(binding) {
                if (checkData(it.size)) {
                    val city = it[args.position]
                    val forecastDay = city.forecastDays
                    val listWeatherHour = viewModel.getWeatherHour24(city)
                    adapterCurrent.submitList(listWeatherHour)
                    rvCurrentDay.adapter = adapterCurrent
                    adapterForecastDays.submitList(forecastDay.forecastDays)
                    rvForecastForDays.adapter = adapterForecastDays
                }
            }
        })
    }

    private fun checkData(sizeList: Int): Boolean {
        Log.d("WeatherFragment_Log", "args.position ${args.position}")
        return sizeList > args.position && sizeList != EMPTY_LIST
    }

    companion object {
        const val EMPTY_LIST = 0


    }

}