package com.example.weatherforecastapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapp.databinding.ActivityPreviewBinding
import com.example.weatherforecastapp.presentation.rvadapter.reAllCities.AllCityAdapter
import com.example.weatherforecastapp.presentation.viewModels.ViewModelAllCities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityAllCities : AppCompatActivity() {

    private val binding by lazy {
        ActivityPreviewBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[ViewModelAllCities::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()

        binding.button.setOnClickListener {

        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.progressBar2.visibility = View.VISIBLE
                    // val city =  apiService . searchCity (city = query ?: "")
                    //  Log.d("ActivityPreview","searchCity| $city")

                    binding.progressBar2.visibility = View.GONE
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    fun init() = with(binding) {
        val adapterAllCities = AllCityAdapter(applicationContext)
        viewModel.listLocation.observe(this@ActivityAllCities, Observer {
            adapterAllCities.submitList(it)
        })
        rvAllCity.adapter = adapterAllCities
        adapterAllCities.onClick = {
            val intent = ActivityWeather.newIntent(this@ActivityAllCities, it)
            startActivity(intent)
        }

    }


    override fun onResume() {
        super.onResume()

    }

    companion object {
        fun newInstance(context: Context) = Intent(context, ActivityAllCities::class.java)

    }
}