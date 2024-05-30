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
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[ViewModelWeather::class.java]
        init()
    }

    private fun init() {
        binding.bMenu.setOnClickListener {
            val action =
                FragmentPagerWeatherDirections.actionFragmentPagerWeatherToFragmentAllCities()
            findNavController().navigate(action)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.cities.collect { stateCity ->
                    when (stateCity) {
                        is StateCity.Empty -> {
                            binding.progressBar2.visibility = View.GONE
                            binding.cardToolbar.visibility = View.GONE
                        }

                        is StateCity.Loading -> {
                            binding.load.visibility = View.VISIBLE
                        }

                        is StateCity.Cities -> {
                            initPager(stateCity)
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.stateNetwork.collect {
                    checkInternet(it)
                    migrationIfNotLocation(it)
                }
            }
        }

    }

    private fun initPager(stateCities: StateCity.Cities) {
        animationViewElements()
        val cities = stateCities.cities
        val lastUpdate =
            "${resources.getString(R.string.the_latest_update)} ${cities[0].location.localtime}"
        binding.tvTheLastUpdate.text = lastUpdate

        Log.d("FragmentPagerWeather_Log", "sizeCity: ${cities.size}")
        val pager = PagerAdapter(requireActivity(), cities.size)
        with(binding) {
            viewPager.adapter = pager
            viewPager.setCurrentItem(args.position, false)
            initTabLayoutMediator(cities)
            viewPager.visibility = View.VISIBLE
        }
    }

    private fun initTabLayoutMediator(cities: List<City>) {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if (cities.size > position && cities[position].location.locationId == USER_POSITION) {
                tab.setIcon(R.drawable.ic_nav)
                tab.view.isClickable = false
            } else {
                tab.setIcon(R.drawable.ic_tochka)
                tab.view.isClickable = false
            }
        }.attach()
        binding.cardToolbar.visibility = View.VISIBLE
    }

    private fun animationViewElements() = with(binding) {
        cvNotInternet.alpha = 0f
        cardToolbar.alpha = 0f
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                cardToolbar.animate()
                    .alpha(1f)
                    .setDuration(DURATION)
                    .start()
                cvNotInternet.animate()
                    .alpha(1f)
                    .setDuration(DURATION)
                    .start()
                load.animate()
                    .alpha(0f)
                    .setDuration(DURATION)
                    .start()
            }
        })
    }

    private fun checkInternet(stateNetwork: StateNetwork) {
        if (stateNetwork.internet) {
            binding.progressBar2.visibility = View.VISIBLE
            binding.cvNotInternet.visibility = View.GONE
            binding.textView3.text = resources.getString(R.string.load_date)
            Log.d("FragmentPagerWeather_Log", "internet: true")
        } else {
            binding.textView3.text = resources.getString(R.string.not_internet)
            binding.cvNotInternet.visibility = View.VISIBLE
        }
    }

    private fun migrationIfNotLocation(stateNetwork: StateNetwork) {
        if (stateNetwork.migration) {
            val action =
                FragmentPagerWeatherDirections.actionFragmentPagerWeatherToFragmentAllCities()
            findNavController().navigate(action)
        }
    }


    companion object {
        private const val USER_POSITION = 0
        private const val DURATION = 100L
    }
}
