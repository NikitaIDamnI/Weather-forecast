package com.example.weatherforecastapp.presentation.activity.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.databinding.FragmentAllCitiesBinding
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.rvadapter.reAllCities.AllCityAdapter
import com.example.weatherforecastapp.presentation.rvadapter.rvSearchCity.SearchCityAdapter
import com.example.weatherforecastapp.presentation.viewModels.ViewModelAllCities
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class FragmentAllCities : Fragment() {

    private var _binding: FragmentAllCitiesBinding? = null
    private val binding: FragmentAllCitiesBinding
        get() = _binding ?: throw RuntimeException("WeatherFragmentBinding = null")

    private lateinit var viewModel: ViewModelAllCities

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
        checkInternet()
        setupAllCitiesAdapter()
        setupSearchAdapter()
    }

    private fun checkInternet() {
        viewModel.internetCondition.observe(viewLifecycleOwner) { internet ->
            if (internet) {
                binding.cvNotInternet.visibility = View.GONE
                binding.cardSearchView.visibility = View.VISIBLE
            } else {
                viewModel.listLocation.observe(viewLifecycleOwner) {
                    val text =
                        "${resources.getString(R.string.the_latest_update)} ${it.get(0).localtime}"
                    binding.tvLastUpdate.text = text
                }
                binding.cvNotInternet.visibility = View.VISIBLE
                binding.cardSearchView.visibility = View.GONE
            }
        }
    }

    private fun setupAllCitiesAdapter() = with(binding) {
        adapterAllCities = AllCityAdapter(requireActivity().applicationContext)
        viewModel.listLocation.observe(viewLifecycleOwner) {
            adapterAllCities.submitList(it)
        }
        rvAllCity.adapter = adapterAllCities
        setupSwipeListener(rvAllCity)
        adapterAllCities.onClick = { id, binding ->
            val action =
                FragmentAllCitiesDirections.actionFragmentAllCitiesToFragmentPagerWeather()
                    .setId(id)
            findNavController().navigate(action)
        }
    }

    private fun setupSearchAdapter() {
        searchCityAdapter = SearchCityAdapter(requireActivity().applicationContext)
        binding.rvSearch.adapter = searchCityAdapter
        searchCityAdapter.onClick =
            { searchCity ->
                viewModel.previewCity(searchCity)
                searchCityAdapter.submitList(emptyList())

                binding.searchView.setQuery(EMPTY_QUERY, false)
                binding.searchView.clearFocus()
                binding.searchView.isIconified = true

                var view = false
                viewModel.listLocation.observe(viewLifecycleOwner) { listLocation ->
                    val position = "${searchCity.lat},${searchCity.lon}"
                    view = viewModel.checkCity(listLocation, position)

                }
                val action =
                    FragmentAllCitiesDirections.actionFragmentAllCitiesToPreviewNewWeatherFragment()
                        .setViewAddCity(view)
                findNavController().navigate(action)
            }

        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (!query.isNullOrEmpty()) {
                            viewModel.searchCity(query)
                            viewModel.searchCityList.observe(viewLifecycleOwner) {
                                searchCityAdapter.submitList(it)
                            }
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

                if (position != USER_POSITION) {
                    viewModel.deleteCity(item)
                } else {
                    adapterAllCities.notifyItemChanged(position)
                }
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return if (viewHolder.adapterPosition == USER_POSITION) {
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
        const val USER_POSITION = 0
        const val DEFAULT_SWIPE_DIRECTION = 0
        const val EMPTY_QUERY = ""
    }

}