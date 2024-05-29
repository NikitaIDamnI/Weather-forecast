package com.example.weatherforecastapp.presentation.activity.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.databinding.FragmentAllCitiesBinding
import com.example.weatherforecastapp.domain.StateCity
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.clickableSpan
import com.example.weatherforecastapp.presentation.rvadapter.reAllCities.AllCityAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvSearchCity.SearchCityAdapter
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import kotlinx.coroutines.launch
import javax.inject.Inject


class FragmentAllCities : Fragment() {

    private var _binding: FragmentAllCitiesBinding? = null
    private val binding: FragmentAllCitiesBinding
        get() = _binding ?: throw RuntimeException("WeatherFragmentBinding = null")

    private lateinit var viewModel: ViewModelWeather

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var searchCityAdapter: SearchCityAdapter
    lateinit var adapterAllCities: AllCityAdapter

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllCitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[ViewModelWeather::class.java]
        checkInternet()
        checkLocationPermission()
        setupAllCitiesAdapter()
        setupSearchAdapter()
        observePreviewCityState()
        observeCityList()
    }

    private fun checkInternet() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.internet) {
                binding.cvNotInternet.visibility = View.GONE
                binding.cardSearchView.visibility = View.VISIBLE
            } else {
                binding.cvNotInternet.visibility = View.VISIBLE
                binding.cardSearchView.visibility = View.GONE
            }
        }
    }

    private fun checkLocationPermission() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (!state.locationPermission) {
                binding.imNotLocation.setImageResource(R.drawable.not_location_permission)
                viewModel.shortNotifications.observe(viewLifecycleOwner) { shortNotifications ->
                    if (shortNotifications) {
                        val textShort = resources.getString(R.string.not_permission_location_short)
                        val textFull = resources.getString(R.string.not_permission_location_full)
                        openNotificationNotLocation(textShort, textFull) {
                            startActivity(
                                Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", context?.packageName, null)
                                )
                            )
                        }
                    } else {
                        val textShort = resources.getString(R.string.not_permission_location_short)
                        closeNotificationNotLocation(textShort)
                    }
                }
            } else {
                viewModel.closeNotification()
                checkingEnabledLocation()
            }
        }
    }

    private fun checkingEnabledLocation() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (!state.location) {
                binding.imNotLocation.setImageResource(R.drawable.ic_not_location_2)
                viewModel.shortNotifications.observe(viewLifecycleOwner) { shortNotifications ->
                    if (shortNotifications) {
                        val textShort = resources.getString(R.string.not_location_short)
                        val textFull = resources.getString(R.string.not_location_full)
                        openNotificationNotLocation(textShort, textFull) {
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        }
                    } else {
                        val textShort = resources.getString(R.string.not_location_short)
                        closeNotificationNotLocation(textShort)
                    }
                }
            } else {
                binding.cvNotLocation.visibility = View.GONE
            }
        }
    }

    private fun openNotificationNotLocation(textShort: String, textFull: String, fuz: () -> Unit) {
        binding.cvNotLocation.visibility = View.VISIBLE
        binding.tvNotLocation.text = textShort
        binding.tvNotLocation.setOnClickListener {
            binding.tvNotLocation.clickableSpan(textFull, CLICKABLE_SPAN_INDEX) {
                fuz.invoke()
            }
            viewModel.openNotification()
        }
    }

    private fun closeNotificationNotLocation(textShort: String) {
        binding.tvNotLocation.setOnClickListener {
            binding.tvNotLocation.text = textShort
            viewModel.closeNotification()
        }
    }

    private fun setupAllCitiesAdapter() {
        adapterAllCities = AllCityAdapter(requireActivity().applicationContext, true)
        binding.rvAllCity.adapter = adapterAllCities
        adapterAllCities.onClick = { position ->
            val action = FragmentAllCitiesDirections.actionFragmentAllCitiesToFragmentPagerWeather()
                .setPosition(position)
            findNavController().navigate(action)
        }
        setupSwipeListener(binding.rvAllCity, true)
    }

    private fun observePreviewCityState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.previewCity.collect { state ->
                    if (state is StateCity.PreviewCity) {
                        val action = FragmentAllCitiesDirections.actionFragmentAllCitiesToPreviewNewWeatherFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun setupSearchAdapter() {
        searchCityAdapter = SearchCityAdapter(requireActivity().applicationContext)
        binding.rvSearch.adapter = searchCityAdapter
        searchCityAdapter.onClick = { searchCity ->
            viewModel.addPreviewCity(searchCity)
            binding.searchView.setQuery(EMPTY_QUERY, false)
            binding.searchView.clearFocus()
            binding.searchView.isIconified = true
        }

        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        lifecycleScope.launch {
                            val searchCities = viewModel.searchCity(query)
                            searchCityAdapter.submitList(searchCities)
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
            }
        )
    }

    private fun setupSwipeListener(rvShopList: RecyclerView, locations: Boolean) {
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
                viewModel.deleteCity(item)
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val item = adapterAllCities.currentList[viewHolder.adapterPosition]
                return if (item.locationId == USER_ID && locations) {
                    DEFAULT_SWIPE_DIRECTION
                } else {
                    super.getSwipeDirs(recyclerView, viewHolder)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun observeCityList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listLocation.collect { cityList ->
                    adapterAllCities.submitList(cityList)
                }
            }
        }
    }

    companion object {
        const val USER_ID = 0
        const val DEFAULT_SWIPE_DIRECTION = 0
        const val EMPTY_QUERY = ""
        const val CLICKABLE_SPAN_INDEX = "settings"
    }
}