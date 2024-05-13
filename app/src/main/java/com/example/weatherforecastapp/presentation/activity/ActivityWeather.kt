package com.example.weatherforecastapp.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.databinding.ActivityWeatherBinding
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.checkingСonnections.PermissionsLauncher
import com.example.weatherforecastapp.presentation.checkingСonnections.WeatherReceiver
import com.example.weatherforecastapp.presentation.viewModels.ViewModelAllCities
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelNetworkStatus
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import javax.inject.Inject

class ActivityWeather : AppCompatActivity() {

    private val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    private lateinit var weatherReceiver: WeatherReceiver

    private lateinit var viewModelWeather: ViewModelWeather
    private lateinit var viewModelAllCities: ViewModelAllCities
    private lateinit var viewModelNetworkStatus: ViewModelNetworkStatus


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
        setContentView(binding.root)
        weatherReceiver = WeatherReceiver(this)
        weatherReceiver.startReceiver()
        initViewModels()
        viewModelAllCities.weatherUpdate()
        initReceiver()
        checkUpdate()
        viewModelNetworkStatus.state.observe(this) {
            Log.d("ActivityWeather_Log", "state: $it")

        }

    }

    private fun checkUpdate() {
        viewModelNetworkStatus.state.observe(this) { state ->
            if (state.internet) {
                if (state.location) {
                Log.d("ActivityWeather_Log", "checkUpdate: ${state.internet}")
                    viewModelAllCities.updateUserLocation()
                }
                viewModelAllCities.weatherUpdate()
            }
        }
    }


    private fun initReceiver() {
        weatherReceiver.getState = {
            viewModelNetworkStatus.getState(it)
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

    private fun initViewModels() {
        viewModelAllCities =
            ViewModelProvider(this, viewModelFactory)[ViewModelAllCities::class.java]
        viewModelWeather =
            ViewModelProvider(this, viewModelFactory)[ViewModelWeather::class.java]
        viewModelNetworkStatus =
            ViewModelProvider(this, viewModelFactory)[ViewModelNetworkStatus::class.java]
    }

    companion object {
        const val EMPTY_LIST = 0
    }

}