package com.example.weatherforecastapp.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherforecastapp.databinding.ActivityWeatherBinding
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.checkingСonnections.PermissionsLauncher
import com.example.weatherforecastapp.presentation.checkingСonnections.WeatherReceiver
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import kotlinx.coroutines.launch
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
        weatherReceiver = WeatherReceiver(this,viewModelWeather)
        weatherReceiver.startReceiver()
        initReceiver()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModelWeather.cities.collect {
                    Log.d("Activity_Log", "flow:${it} ")
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModelWeather.stateNetwork.collect {
                    Log.d("Activity_Log", "state:${it} ")
                }
            }
        }

    }

    private fun initReceiver() {
        weatherReceiver.getState = { stateReceiver ->
            viewModelWeather.getState(stateReceiver)
            Log.d("ActivityWeather_Log", "stateReceiver: $stateReceiver")
        }

    }

    override fun onResume() {
        super.onResume()
        permission.isPermissionGranted(PermissionsLauncher.PERMISSION_LOCATION)
        viewModelWeather.checkLocation(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        weatherReceiver.stopReceiver()
    }


}