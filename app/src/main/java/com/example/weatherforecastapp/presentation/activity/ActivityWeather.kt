package com.example.weatherforecastapp.presentation.activity

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
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

    private val weatherReceiver = WeatherReceiver()

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


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        permission.checkPermissions(PermissionsLauncher.PERMISSION_LOCATION)
        super.onCreate(savedInstanceState)
        startReceiver()
        initReceiver()
        viewModelAllCities =
            ViewModelProvider(this, viewModelFactory)[ViewModelAllCities::class.java]
        viewModelWeather =
            ViewModelProvider(this, viewModelFactory)[ViewModelWeather::class.java]

        viewModelAllCities.startCheckInternet()
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun startReceiver() {
        val intentFilter = IntentFilter().apply {
            addAction(WeatherReceiver.ACTION_LOCATION)
        }
        registerReceiver(weatherReceiver, intentFilter, RECEIVER_EXPORTED)
    }

    private fun initReceiver() {
        weatherReceiver.checkLocationReceiver = {
            viewModelWeather.checkLocationCondition(it)
            viewModelAllCities.checkLocationCondition(it)
        }
    }

    override fun onResume() {
        super.onResume()
        permission.isPermissionGranted(PermissionsLauncher.PERMISSION_LOCATION)

        viewModelAllCities.checkLocation(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModelAllCities.stopCheckInternet()
        unregisterReceiver(weatherReceiver)
    }

    companion object {
        const val EMPTY_LIST = 0
    }

}