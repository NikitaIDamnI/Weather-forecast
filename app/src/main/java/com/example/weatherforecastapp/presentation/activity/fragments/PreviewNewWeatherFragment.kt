package com.example.weatherforecastapp.presentation.activity.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.databinding.PreviewNewWeatherFragmentBinding
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation
import com.example.weatherforecastapp.presentation.rvadapter.rvCurrentDay.CurrentAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvForecastDays.ForecastDaysAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvPrecipitation.PrecipitationAdapter
import com.example.weatherforecastapp.presentation.viewModels.ViewModelAllCities
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class PreviewNewWeatherFragment : BottomSheetDialogFragment() {

    private var _binding: PreviewNewWeatherFragmentBinding? = null
    private val binding: PreviewNewWeatherFragmentBinding
        get() = _binding ?: throw RuntimeException("PreviewNewWeatherFragmentBinding = null")


    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[ViewModelAllCities::class.java]
    }

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PreviewNewWeatherFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.setOnShowListener { dialog ->
            val d = dialog as? BottomSheetDialog
            d?.let {
                val bottomSheetInternal =
                    d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetInternal!!)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.peekHeight =
                    resources.displayMetrics.heightPixels // Устанавливаем высоту экрана
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCurrent()
        initForecast()
    }


    private fun initCurrent() {
        val adapterPrecipitation = PrecipitationAdapter(requireContext())
        with(binding) {
            viewModel.city.observe(viewLifecycleOwner, Observer {
                tvCity.text = it.location.name
                tvData.text = it.current.date
                val temp = it.current.temp_c.toInt().toString() + WeatherPrecipitation.VALUE_DEGREE
                tvDegree.text = temp
                tvCondition.text = it.current.condition.text
                Picasso.get().load(it.current.condition.icon).into(imWeather)
                val listPrecipitation = it.current.weatherPrecipitation
                Log.d("PreviewNewWeatherFragment_Log", "initCurrent: $listPrecipitation")
                adapterPrecipitation.submitList(listPrecipitation)
                setupPullAdapter(rvPrecipitation)
                rvPrecipitation.adapter = adapterPrecipitation
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
        viewModel.city.observe(viewLifecycleOwner, Observer {
            with(binding) {
                val listWeatherHour =
                    viewModel.getWeatherHour24(it.location.localtime, it.forecastDays)
                adapterCurrent.submitList(listWeatherHour)
                rvCurrentDay.adapter = adapterCurrent
                adapterForecastDays.submitList(it.forecastDays)
                rvForecastForDays.adapter = adapterForecastDays
            }
        })
    }


}