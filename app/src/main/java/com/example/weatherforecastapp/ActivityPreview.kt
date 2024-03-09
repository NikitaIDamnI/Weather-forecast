package com.example.weatherforecastapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecastapp.databinding.ActivityPreviewBinding

class ActivityPreview : AppCompatActivity() {

    private val binding by lazy {
        ActivityPreviewBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            val intent = ActivityWeather.newIntent(this,1)
            startActivity(intent)
        }


    }
}