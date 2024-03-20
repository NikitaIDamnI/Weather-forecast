package com.example.weatherforecastapp.presentation.activity

import PagerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.data.gps.PermissionsLauncher
import com.example.weatherforecastapp.databinding.ActivityWeatherBinding
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import com.google.android.material.tabs.TabLayoutMediator

class ActivityWeather : AppCompatActivity() {

    private val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[ViewModelWeather::class.java]
    }
    private val permission by lazy {
        PermissionsLauncher(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        permission
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initial()
        //viewModel.getCity(parseArg())
    }

    private fun initial() {
        viewModel.sizeCity.observe(this) {
            binding.viewPager.adapter = PagerAdapter(this, it)

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.setIcon(R.drawable.ic_nav)

                    }

                    else -> {
                        tab.setIcon(R.drawable.ic_tochka)
                    }
                }
            }.attach()

            binding.bMenu.setOnClickListener {
                val intent = ActivityPreview.newInstance(this)
                startActivity(intent)
            }
        }
    }


    private fun parseArg(): Int {
        return intent.getIntExtra(CITY_ID, 0)
    }

    override fun onResume() {
        viewModel.checkLocation()
        super.onResume()
    }

    companion object {
        private const val CITY_ID = "city_id"
        fun newIntent(context: Context, idCity: Int): Intent {
            val intent = Intent(context, ActivityWeather::class.java)
            intent.putExtra(CITY_ID, idCity)
            return intent
        }
    }
}