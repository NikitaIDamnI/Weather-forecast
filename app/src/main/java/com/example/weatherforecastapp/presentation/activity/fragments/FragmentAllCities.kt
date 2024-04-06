package com.example.weatherforecastapp.presentation.activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.databinding.FragmentAllCitiesBinding
import com.example.weatherforecastapp.presentation.rvadapter.reAllCities.AllCityAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvSearchCity.SearchCityAdapter
import com.example.weatherforecastapp.presentation.viewModels.ViewModelAllCities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentAllCities : Fragment() {

    private var _binding: FragmentAllCitiesBinding? = null
    private val binding: FragmentAllCitiesBinding
        get() = _binding ?: throw RuntimeException("WeatherFragmentBinding = null")


    private val viewModel by lazy {
        ViewModelProvider(this)[ViewModelAllCities::class.java]
    }
    lateinit var searchCityAdapter: SearchCityAdapter
    lateinit var adapterAllCities: AllCityAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllCitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAllCitiesAdapter()
        setupSearchAdapter()
    }

    private fun setupAllCitiesAdapter() = with(binding) {
        adapterAllCities = AllCityAdapter(requireActivity().applicationContext)
        viewModel.listLocation.observe(viewLifecycleOwner, Observer {
            adapterAllCities.submitList(it)
        })
        rvAllCity.adapter = adapterAllCities
        setupSwipeListener(rvAllCity)
        adapterAllCities.onClick = { id, binding ->
            val action = FragmentAllCitiesDirections.actionFragmentAllCitiesToFragmentPagerWeather().setId(id)
            findNavController().navigate(action)

        }
    }

    private fun setupSearchAdapter() {

        searchCityAdapter = SearchCityAdapter(requireActivity().applicationContext)
        binding.rvSearch.adapter = searchCityAdapter
        searchCityAdapter.onClick = {
            //viewModel.addCity(it)
            val action = FragmentAllCitiesDirections.actionFragmentAllCitiesToPreviewNewWeatherFragment()
            findNavController().navigate(action)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!query.isNullOrEmpty()) {
                        viewModel.searchCity(query)
                        viewModel.searchCityList.observe(viewLifecycleOwner, Observer {
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


}