package com.example.weatherforecastapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.databinding.ActivityWeatherBinding
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.checkingСonnections.PermissionsLauncher
import com.example.weatherforecastapp.presentation.checkingСonnections.WeatherReceiver
import com.example.weatherforecastapp.presentation.viewModels.ViewModelAllCities
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import javax.inject.Inject

class ActivityWeather : AppCompatActivity() {

    private val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    private lateinit var weatherReceiver : WeatherReceiver

    private lateinit var viewModelWeather: ViewModelWeather
    private lateinit var viewModelAllCities: ViewModelAllCities


    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private val permission by lazy {
        PermissionsLauncher(this)
    }

    private val component by lazy {
        (application as WeatherApp).component
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        permission.checkPermissions(PermissionsLauncher.PERMISSION_LOCATION)
        super.onCreate(savedInstanceState)
        weatherReceiver = WeatherReceiver(this)
        weatherReceiver.startReceiver()

        initReceiver()
        viewModelAllCities =
            ViewModelProvider(this, viewModelFactory)[ViewModelAllCities::class.java]
        viewModelWeather =
            ViewModelProvider(this, viewModelFactory)[ViewModelWeather::class.java]

        setContentView(binding.root)
        viewModelAllCities.internetCondition.observe(this) { internet ->
            if (internet) {
                viewModelAllCities.sizeCity.observe(this) {
                    if (it == EMPTY_LIST) {
                        viewModelAllCities.updateUserLocation()
                    } else {
                        viewModelAllCities.weatherUpdate()
                    }
                }
            }
        }
    }


    private fun initReceiver() {
        weatherReceiver.checkLocationCondition = {
            viewModelWeather.checkLocationCondition(it)
            viewModelAllCities.checkLocationCondition(it)
        }
        weatherReceiver.checkInternetCondition = {
            viewModelAllCities.checkInternetCondition(it)
        }
    }

    override fun onResume() {
        super.onResume()
        permission.isPermissionGranted(PermissionsLauncher.PERMISSION_LOCATION)

        viewModelAllCities.checkLocation(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        weatherReceiver.stopReceiver()
    }

    companion object {
        const val EMPTY_LIST = 0
    }

}