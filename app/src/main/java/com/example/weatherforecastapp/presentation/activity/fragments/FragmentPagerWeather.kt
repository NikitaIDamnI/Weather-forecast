package com.example.weatherforecastapp.presentation.activity.fragments

import PagerAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.databinding.FragmentPagerWeatherBinding
import com.example.weatherforecastapp.presentation.WeatherApp
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
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
        migrationIfNotLocation()
        initial()
    }

    private fun initial() {
        animationViewElements()
        binding.bMenu.setOnClickListener {
            val action =
                FragmentPagerWeatherDirections.actionFragmentPagerWeatherToFragmentAllCities()
            findNavController().navigate(action)
        }

        viewModel.sizeCity.observe(viewLifecycleOwner) {

            Log.d("FragmentPagerWeather_Log", "sizeCity: $it")
            val pager = PagerAdapter(requireActivity(), it)

            with(binding) {

                viewPager.adapter = pager
                viewPager.setCurrentItem(args.position, false)

                initTabLayoutMediator()


                if (it == EMPTY_LIST) {
                    viewPager.visibility = View.GONE
                } else {
                    viewPager.visibility = View.VISIBLE
                }

                checkInternet(it)
            }
        }

    }

    private fun initTabLayoutMediator() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

            viewModel.listLocation.observe(viewLifecycleOwner) { listLocation ->
                if (listLocation.size > position && listLocation[position].locationId == USER_POSITION) {
                    tab.setIcon(R.drawable.ic_nav)
                    tab.view.isClickable = false
                } else {
                    tab.setIcon(R.drawable.ic_tochka)
                    tab.view.isClickable = false
                }
            }
        }
            .attach()
    }

    private fun checkInternet(size: Int) {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            Log.d("FragmentPagerWeather_Log", "internetCondition: ${state.internet}")
            if (state.internet) {
                onInternetAvailable()
            } else {
                onInternetUnavailable(size)
            }
        }

    }

    private fun onInternetUnavailable(sizeList: Int) {
        if (sizeList == EMPTY_LIST) {
            binding.progressBar2.visibility = View.GONE
            binding.textView3.text = resources.getString(R.string.not_internet)
        } else {
            binding.cvNotInternet.visibility = View.VISIBLE
            viewModel.listLocation.observe(viewLifecycleOwner, Observer {
                val lastUpdate =
                    "${resources.getString(R.string.the_latest_update)} ${it[0].localtime}"
                binding.tvTheLastUpdate.text = lastUpdate
            })
        }
    }

    private fun onInternetAvailable() {
        binding.cvNotInternet.visibility = View.GONE
        binding.progressBar2.visibility = View.VISIBLE
        binding.textView3.text = resources.getString(R.string.load_date)

        Log.d("FragmentPagerWeather_Log", "internet: true")
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


    private fun migrationIfNotLocation() {
        viewModel.sizeCity.observe(viewLifecycleOwner) { size ->
            if (size == 0) {
                viewModel.state.observe(viewLifecycleOwner) { state ->
                    if (state.internet) {
                        if (!state.location) {
                            val action =
                                FragmentPagerWeatherDirections.actionFragmentPagerWeatherToFragmentAllCities()
                            findNavController().navigate(action)
                        }
                    }
                }
            }

        }

    }


    companion object {
        private const val START_POSITION = 0
        private const val USER_POSITION = 0
        private const val EMPTY_LIST = 0
        private const val DURATION = 100L
    }
}
