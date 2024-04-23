package com.example.weatherforecastapp.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.data.gps.PermissionsLauncher
import com.example.weatherforecastapp.databinding.ActivityWeatherBinding
import com.example.weatherforecastapp.presentation.InternetConnectionChecker
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import javax.inject.Inject

class ActivityWeather : AppCompatActivity() {

    private val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    lateinit var internetConnectionChecker: InternetConnectionChecker

    private lateinit var viewModel: ViewModelWeather

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
        permission
        super.onCreate(savedInstanceState)
        internetConnectionChecker = InternetConnectionChecker(this)
        internetConnectionChecker.startListening()
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelWeather::class.java]
        setContentView(binding.root)
        internetConnectionChecker.update = {
            Log.d("FragmentPagerWeather_Log", "update: true")
            viewModel.weatherUpdate()
        }


    }


    override fun onResume() {
        super.onResume()
        viewModel.checkLocation(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        internetConnectionChecker.stopListening()

    }

}