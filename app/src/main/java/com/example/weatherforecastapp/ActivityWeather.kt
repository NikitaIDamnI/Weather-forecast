package com.example.weatherforecastapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecastapp.databinding.ActivityWeatherBinding
import com.example.weatherforecastapp.presentation.pager.PagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ActivityWeather : AppCompatActivity() {

    private val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initial()
    }

    private fun initial() {
        binding.viewPager.adapter = PagerAdapter(this)
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


    fun parseArg(): Int {
        return intent.getIntExtra(CITY_ID, 0)
    }

    override fun onResume() {
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