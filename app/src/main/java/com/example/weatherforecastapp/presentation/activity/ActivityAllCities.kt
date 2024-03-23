package com.example.weatherforecastapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecastapp.databinding.ActivityPreviewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityAllCities : AppCompatActivity() {

    private val binding by lazy {
        ActivityPreviewBinding.inflate(layoutInflater)
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


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


    override fun onResume() {
        super.onResume()

    }
companion object{
    fun newInstance(context: Context) = Intent(context, ActivityAllCities::class.java)

}
}