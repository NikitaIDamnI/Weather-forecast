package com.example.weatherforecastapp.presentation.activity.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.databinding.FragmentAllCitiesBinding
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.clickableSpan
import com.example.weatherforecastapp.presentation.rvadapter.reAllCities.AllCityAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvSearchCity.SearchCityAdapter
import com.example.weatherforecastapp.presentation.viewModels.ViewModelAllCities
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelNetworkStatus
import kotlinx.coroutines.launch
import javax.inject.Inject


class FragmentAllCities : Fragment() {

    private var _binding: FragmentAllCitiesBinding? = null
    private val binding: FragmentAllCitiesBinding
        get() = _binding ?: throw RuntimeException("WeatherFragmentBinding = null")

    private lateinit var viewModel: ViewModelAllCities
    private lateinit var viewModelNetworkStatus: ViewModelNetworkStatus

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
            ViewModelProvider(requireActivity(), viewModelFactory)[ViewModelAllCities::class.java]
        viewModelNetworkStatus =
            ViewModelProvider(
                requireActivity(),
                viewModelFactory
            )[ViewModelNetworkStatus::class.java]
        checkInternet()
        checkLocationPermission()
        setupAllCitiesAdapter()
        setupSearchAdapter()
    }

    private fun checkInternet() {

        viewModelNetworkStatus.state.observe(viewLifecycleOwner) { state ->
            if (state.internet) {
                binding.cvNotInternet.visibility = View.GONE
                binding.cardSearchView.visibility = View.VISIBLE
            } else {
                viewModel.listLocation.observe(viewLifecycleOwner) {
                    if (it.isNotEmpty()) {
                        val text =
                            "${resources.getString(R.string.the_latest_update)} ${it[0].localtime}"
                        binding.tvLastUpdate.text = text
                    }
                }
                binding.cvNotInternet.visibility = View.VISIBLE
                binding.cardSearchView.visibility = View.GONE
            }
        }
    }

    private fun checkLocationPermission() {
        viewModelNetworkStatus.state.observe(viewLifecycleOwner) { state ->
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
                                    Uri.fromParts("package", context?.packageName, null),
                                ),
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
        viewModelNetworkStatus.state.observe(viewLifecycleOwner) { state ->
            Log.d("FragmentAllCities_Log", "locationCondition: ${state.location})")
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


    private fun setupAllCitiesAdapter() = with(binding) {
        adapterAllCities = AllCityAdapter(requireActivity().applicationContext, true)
        adapterAllCities.onClick = { position ->
            val action =
                FragmentAllCitiesDirections.actionFragmentAllCitiesToFragmentPagerWeather()
                    .setPosition(position)
            findNavController().navigate(action)
        }

        viewModelNetworkStatus.state.observe(viewLifecycleOwner) { state ->
            adapterAllCities = AllCityAdapter(requireActivity().applicationContext, state.internet)
            viewModel.listLocation.observe(viewLifecycleOwner) {
                adapterAllCities.submitList(it)
            }
            adapterAllCities.onClick = { position ->
                val action =
                    FragmentAllCitiesDirections.actionFragmentAllCitiesToFragmentPagerWeather()
                        .setPosition(position)
                findNavController().navigate(action)
            }
            rvAllCity.adapter = adapterAllCities
            setupSwipeListener(rvAllCity, state.location)
        }


    }

    private fun setupSearchAdapter() {
        searchCityAdapter = SearchCityAdapter(requireActivity().applicationContext)
        binding.rvSearch.adapter = searchCityAdapter
        searchCityAdapter.onClick = { searchCity ->
            viewModel.previewCity(searchCity)

            binding.searchView.setQuery(EMPTY_QUERY, false)
            binding.searchView.clearFocus()
            binding.searchView.isIconified = true

            var view = false
            viewModel.listLocation.observe(viewLifecycleOwner) { listLocation ->
                view = viewModel.checkCity(listLocation, searchCity)
            }
            val action =
                FragmentAllCitiesDirections.actionFragmentAllCitiesToPreviewNewWeatherFragment()
                    .setViewAddCity(view)
            findNavController().navigate(action)
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

    companion object {
        const val USER_ID = 0
        const val DEFAULT_SWIPE_DIRECTION = 0
        const val EMPTY_QUERY = ""
        const val CLICKABLE_SPAN_INDEX = "settings"
    }

}