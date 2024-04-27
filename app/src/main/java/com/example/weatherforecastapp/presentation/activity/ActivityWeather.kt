package com.example.weatherforecastapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.data.gps.PermissionsLauncher
import com.example.weatherforecastapp.databinding.ActivityWeatherBinding
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.viewModels.ViewModelAllCities
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import javax.inject.Inject

class ActivityWeather : AppCompatActivity() {

    private val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }


    private lateinit var viewModel: ViewModelAllCities

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
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelAllCities::class.java]
        viewModel.startCheckInternet()
        setContentView(binding.root)
        viewModel.internetCondition.observe(this) {internet->
            if (internet)
            viewModel.sizeCity.observe(this) {
                if (it == EMPTY_LIST){
                    viewModel.updateUserLocation()
                }else{
                    viewModel.weatherUpdate()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.checkLocation(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopCheckInternet()

    }

    companion object{
        const val EMPTY_LIST = 0
    }

}