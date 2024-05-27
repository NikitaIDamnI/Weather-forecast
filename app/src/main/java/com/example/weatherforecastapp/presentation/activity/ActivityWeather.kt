package com.example.weatherforecastapp.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.databinding.ActivityWeatherBinding
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.checkingСonnections.PermissionsLauncher
import com.example.weatherforecastapp.presentation.checkingСonnections.WeatherReceiver
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import javax.inject.Inject

class ActivityWeather : AppCompatActivity() {

    private val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    private lateinit var weatherReceiver: WeatherReceiver

    private lateinit var viewModelWeather: ViewModelWeather


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
        viewModelWeather =
            ViewModelProvider(this, viewModelFactory)[ViewModelWeather::class.java]
        weatherReceiver = WeatherReceiver(this)
        weatherReceiver.startReceiver()
        initReceiver()
        checkUpdate()
    }

    private fun initReceiver() {
        weatherReceiver.getState = { stateReceiver ->
            viewModelWeather.getState(stateReceiver)
            Log.d("ActivityWeather_Log", "stateReceiver: $stateReceiver")
        }

    }

    private fun checkUpdate() {
        viewModelWeather.state.observe(this) { state ->
            if (state.internet) {
                if (state.location) {
                    Log.d("ActivityWeather_Log", "checkUpdate: ${state.internet}")
                    viewModelWeather.updateUserLocation()
                }
                viewModelWeather.weatherUpdate()
            }

        }
    }


    override fun onResume() {
        super.onResume()
        permission.isPermissionGranted(PermissionsLauncher.PERMISSION_LOCATION)
        viewModelWeather.checkLocation(this)
        checkUpdate()
    }


    override fun onDestroy() {
        super.onDestroy()
        weatherReceiver.stopReceiver()
    }


    companion object {
        const val EMPTY_LIST = 0
    }

}