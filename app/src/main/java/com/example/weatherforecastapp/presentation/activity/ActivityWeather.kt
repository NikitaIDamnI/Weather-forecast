package com.example.weatherforecastapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.data.gps.PermissionsLauncher
import com.example.weatherforecastapp.databinding.ActivityWeatherBinding
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather

class ActivityWeather : AppCompatActivity() {

    private val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }


    private val viewModelWeather by lazy {
        ViewModelProvider(this)[ViewModelWeather::class.java]
    }


    private val permission by lazy {
        PermissionsLauncher(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        permission
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModelWeather.weatherUpdate()
    }





    override fun onResume() {
        super.onResume()
        viewModelWeather.checkLocation(this)
    }


}