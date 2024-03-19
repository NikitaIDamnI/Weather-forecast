package com.example.weatherforecastapp.presentation.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.databinding.WeatherFragmentBinding
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather

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
        initView()

    }

    private fun initView() = with(binding) {
        viewModel.city.observe(viewLifecycleOwner) {
            tvCity.text = it.location.name
            tvData.text = it.current.last_updated_epoch.toString()
        }
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