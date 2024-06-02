package com.example.weatherforecastapp.presentation.activity.fragments

import PagerAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.databinding.FragmentPagerWeatherBinding
import com.example.weatherforecastapp.domain.StateCity
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.checkingСonnections.StateNetwork
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import javax.inject.Inject


class FragmentPagerWeather : Fragment() {

    private var _binding: FragmentPagerWeatherBinding? = null
    private val binding: FragmentPagerWeatherBinding
        get() = _binding ?: throw RuntimeException("FragmentPagerWeatherBinding = null")

    private val args by navArgs<FragmentPagerWeatherArgs>()
    private lateinit var viewModel: ViewModelWeather

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    private lateinit var pagerAdapter: PagerAdapter

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagerWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[ViewModelWeather::class.java]
        init()
    }

    private fun init() {
        binding.bMenu.setOnClickListener {
            val action =
                FragmentPagerWeatherDirections.actionFragmentPagerWeatherToFragmentAllCities()
            findNavController().navigate(action)
        }
        setupPager()
        setupObservers()
    }

    private fun setupPager() {
        pagerAdapter = PagerAdapter(requireActivity())
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if (position== USER_POSITION){
                tab.setIcon(R.drawable.ic_nav)
                tab.view.isClickable = false
            }else {
                tab.setIcon(R.drawable.ic_tochka)
                tab.view.isClickable = false
            }
        }.attach()
        binding.cardToolbar.visibility = View.VISIBLE
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.cities.collect { stateCity ->
                    Log.d(TAG, "stateCity: $stateCity")
                    when (stateCity) {
                        is StateCity.Empty -> showLoading(true)
                        is StateCity.Loading -> showLoading(true)
                        is StateCity.Cities -> {
                            checkInternet()
                            showLoading(false)
                            updatePager(stateCity.cities)
                        }
                        else->{}
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.stateNetwork.collect {
                    Log.d(TAG, "stateNetwork: $it")
                    migrationIfNotLocation(it)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.load.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.cardToolbar.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun updatePager(cities: List<City>) {
        pagerAdapter.submitList(cities)
        binding.tvTheLastUpdate.text = "${resources.getString(R.string.the_latest_update)} ${cities[0].location.localtime}"
        binding.viewPager.setCurrentItem(args.position, false)
    }

    private fun migrationIfNotLocation(stateNetwork: StateNetwork) {
        Log.d(TAG, "migrationIfNotLocation: ${stateNetwork.migration}")
        if (stateNetwork.migration) {
            val action =
                FragmentPagerWeatherDirections.actionFragmentPagerWeatherToFragmentAllCities()
            findNavController().navigate(action)
        }
    }
    private fun checkInternet() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.stateNetwork.collect { stateNetwork ->
                    if (stateNetwork.internet) {
                        binding.progressBar2.visibility = View.VISIBLE
                        binding.cvNotInternet.visibility = View.GONE
                        binding.textView3.text = resources.getString(R.string.load_date)
                    } else {
                        binding.textView3.text = resources.getString(R.string.not_internet)
                        binding.progressBar2.visibility = View.GONE
                        binding.cvNotInternet.visibility = View.VISIBLE

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "FragmentPagerWeather_Log"
        private const val USER_POSITION = 0
        private const val DURATION = 100L
    }
}