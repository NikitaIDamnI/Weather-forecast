package com.example.weatherforecastapp.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.databinding.ActivityAllCitiesBinding
import com.example.weatherforecastapp.presentation.rvadapter.reAllCities.AllCityAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvSearchCity.SearchCityAdapter
import com.example.weatherforecastapp.presentation.viewModels.ViewModelAllCities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityAllCities : AppCompatActivity() {

    private val binding by lazy {
        ActivityAllCitiesBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[ViewModelAllCities::class.java]
    }
    lateinit var searchCityAdapter: SearchCityAdapter
    lateinit var adapterAllCities: AllCityAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupAllCitiesAdapter()
        setupSearchAdapter()

    }


    private fun setupAllCitiesAdapter() = with(binding) {
        adapterAllCities = AllCityAdapter(applicationContext)
        viewModel.listLocation.observe(this@ActivityAllCities, Observer {
            adapterAllCities.submitList(it)
        })
        rvAllCity.adapter = adapterAllCities
        setupSwipeListener(rvAllCity)
        adapterAllCities.onClick = { id, binding ->
            val intent = ActivityWeather.newIntent(this@ActivityAllCities,id,true)

            startActivity(intent)
            finish()
        }
    }

    private fun setupSearchAdapter() {

        searchCityAdapter = SearchCityAdapter(this@ActivityAllCities)
        binding.rvSearch.adapter = searchCityAdapter
        searchCityAdapter.onClick = {
            viewModel.addCity(it)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!query.isNullOrEmpty()) {
                        viewModel.searchCity(query)
                        viewModel.searchCityList.observe(this@ActivityAllCities, Observer {
                            searchCityAdapter.submitList(it)
                        })
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    searchCityAdapter.submitList(emptyList())
                }
                return true
            }

        })
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapterAllCities.currentList[position]

                if (position != 0) {
                    viewModel.deleteCity(item)
                } else {
                    adapterAllCities.notifyItemChanged(position)
                }
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return if (viewHolder.adapterPosition == 0) 0 else super.getSwipeDirs(
                    recyclerView,
                    viewHolder
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }


    companion object {
        const val EXTRA_ID = "extra_it"

        fun newInstance(context: Context) = Intent(context, ActivityAllCities::class.java)
    }


}