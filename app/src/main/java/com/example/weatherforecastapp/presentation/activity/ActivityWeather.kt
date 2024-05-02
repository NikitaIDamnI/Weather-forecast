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
import javax.inject.Inject

class ActivityWeather : AppCompatActivity() {

    private val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    private val weatherReceiver = WeatherReceiver()

    private lateinit var viewModel: ViewModelAllCities

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
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelAllCities::class.java]
        viewModel.startCheckInternet()
        setContentView(binding.root)
        viewModel.internetCondition.observe(this) {internet->
            if (internet) {
                viewModel.sizeCity.observe(this) {
                    if (it == EMPTY_LIST) {
                        viewModel.updateUserLocation()
                    } else {
                        viewModel.weatherUpdate()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun startReceiver(){
        val intentFilter = IntentFilter().apply {
            addAction(WeatherReceiver.ACTION_LOCATION)
        }
        registerReceiver(weatherReceiver, intentFilter, RECEIVER_EXPORTED)
    }

    override fun onResume() {
        super.onResume()
        permission.isPermissionGranted(PermissionsLauncher.PERMISSION_LOCATION)

        viewModel.checkLocation(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopCheckInternet()
        unregisterReceiver(weatherReceiver)
    }

    companion object{
        const val EMPTY_LIST = 0
    }

}