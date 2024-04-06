package com.example.weatherforecastapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.data.gps.PermissionsLauncher
import com.example.weatherforecastapp.databinding.ActivityWeatherBinding
import com.example.weatherforecastapp.presentation.viewModels.ViewModelAllCities
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactoryWeather
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather

class ActivityWeather : AppCompatActivity() {

    private val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    private val viewModelFactoryWeather by lazy {
        ViewModelFactoryWeather(application, 0)
    }
    private val viewModelWeather by lazy {
        ViewModelProvider(this, viewModelFactoryWeather)[ViewModelWeather::class.java]
    }

    private val viewModelAllCities by lazy {
        ViewModelProvider(this)[ViewModelAllCities::class.java]
    }
    private val permission by lazy {
        PermissionsLauncher(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        permission
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initial()
    }

    private fun initial() {
        viewModelWeather.weatherUpdate()

    }


    override fun onResume() {
        super.onResume()
        viewModelWeather.checkLocation(this)
    }


}