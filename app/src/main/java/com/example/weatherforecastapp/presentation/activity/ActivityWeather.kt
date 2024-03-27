package com.example.weatherforecastapp.presentation.activity

import PagerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.data.gps.PermissionsLauncher
import com.example.weatherforecastapp.databinding.ActivityWeatherBinding
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import com.google.android.material.tabs.TabLayoutMediator

class ActivityWeather : AppCompatActivity() {

    private val binding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    private val viewModelFactory by lazy {
        ViewModelFactory(application,parseArg())
    }
    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[ViewModelWeather::class.java]
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
        animationToolbar()
        binding.bMenu.setOnClickListener {
            val intent = ActivityAllCities.newInstance(this)
            startActivity(intent)
            finish()
        }
        viewModel.sizeCity.observe(this) {

            val pager = PagerAdapter(this, it)

            with(binding) {
                viewPager.adapter = pager
                viewPager.setCurrentItem(parseArg(), true)

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

                if (it == 0){
                    viewPager.visibility = View.GONE

                }

                viewPager.visibility = View.VISIBLE
            }
        }

    }

    private fun animationToolbar() = with(binding){
        cardToolbar.alpha = 0f
        viewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                root.visibility = View.VISIBLE
                cardToolbar.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .start()
            }
        })
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
        fun newIntent(context: Context, idCity: Int): Intent{
            val intent = Intent(context, ActivityWeather::class.java)
            intent.putExtra(CITY_ID, idCity)
            return intent
        }
    }
}