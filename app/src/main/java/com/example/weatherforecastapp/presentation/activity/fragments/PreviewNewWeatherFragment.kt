package com.example.weatherforecastapp.presentation.activity.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.databinding.PreviewNewWeatherFragmentBinding
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation
import com.example.weatherforecastapp.presentation.rvadapter.rvCurrentDay.CurrentAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvForecastDays.ForecastDaysAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvPrecipitation.PrecipitationAdapter
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class PreviewNewWeatherFragment : BottomSheetDialogFragment() {

    private var _binding: PreviewNewWeatherFragmentBinding? = null
    private val binding: PreviewNewWeatherFragmentBinding
        get() = _binding ?: throw RuntimeException("PreviewNewWeatherFragmentBinding = null")

    private val args by navArgs<WeatherFragmentArgs>()


    private val viewModelFactory by lazy {
        ViewModelFactory(requireActivity().application, args.id)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ViewModelWeather::class.java]
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PreviewNewWeatherFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.setOnShowListener { dialog ->
            val d = dialog as? BottomSheetDialog
            d?.let {
                val bottomSheetInternal = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetInternal!!)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.peekHeight = resources.displayMetrics.heightPixels // Устанавливаем высоту экрана
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
                val listWeatherHour = viewModel.getWeatherHour24(it)
                adapterCurrent.submitList(listWeatherHour)
                rvCurrentDay.adapter = adapterCurrent
                adapterForecastDays.submitList(it)
                rvForecastForDays.adapter = adapterForecastDays
            }
        })
    }


}