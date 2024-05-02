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
import com.example.weatherforecastapp.presentation.viewModels.ViewModelAllCities
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject


class FragmentPagerWeather : Fragment() {

    private var _binding: FragmentPagerWeatherBinding? = null
    private val binding: FragmentPagerWeatherBinding
        get() = _binding ?: throw RuntimeException("FragmentPagerWeatherBinding = null")

    private val args by navArgs<FragmentPagerWeatherArgs>()


    private lateinit var viewModel: ViewModelAllCities

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
            ViewModelProvider(requireActivity(), viewModelFactory)[ViewModelAllCities::class.java]
        initial()
    }

    private fun initial() {
        animationToolbar()
        binding.bMenu.setOnClickListener {
            val action =
                FragmentPagerWeatherDirections.actionFragmentPagerWeatherToFragmentAllCities()
            findNavController().navigate(action)
        }

        viewModel.sizeCity.observe(viewLifecycleOwner) {
            val argsList = getListArgs(it)
            Log.d("FragmentPagerWeather_Log", "sizeCity: $it")
            val pager = PagerAdapter(requireActivity(), argsList)

            with(binding) {

                viewPager.adapter = pager
                viewPager.setCurrentItem(args.id, false)

                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                    when (position) {
                        USER_POSITION -> {
                            tab.setIcon(R.drawable.ic_nav)
                            tab.view.isClickable = false
                        }

                        else -> {
                            tab.setIcon(R.drawable.ic_tochka)
                            tab.view.isClickable = false
                        }
                    }
                }
                    .attach()


                if (it == EMPTY_LIST) {
                    viewPager.visibility = View.GONE
                } else {
                    viewPager.visibility = View.VISIBLE
                }

                checkInternet(it)
            }
        }

    }

    private fun checkInternet(size: Int) {
        viewModel.internetCondition.observe(viewLifecycleOwner, Observer {
            Log.d("FragmentPagerWeather_Log", "internetCondition: $it")
            if (it) {
                onInternetAvailable()
            } else {
                onInternetUnavailable(size)
            }
        })

    }

    private fun onInternetUnavailable(sizeList: Int) {

        if (sizeList == EMPTY_LIST) {
            binding.progressBar2.visibility = View.GONE
            binding.textView3.text = resources.getString(R.string.not_internet)
        }else {
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

    private fun animationToolbar() = with(binding) {
        cardToolbar.alpha = 0f
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                cardToolbar.animate()
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


    private fun getListArgs(idCity: Int): List<Bundle> {
        val argsList = mutableListOf<Bundle>()
        return if (idCity != EMPTY_LIST) {
            for (id in START_CITY_ID..<idCity) {
                val args = Bundle().apply {
                    putInt("id", id)
                }
                argsList.add(args)
            }
            Log.d("FragmentPagerWeather", "argsList.size: ${argsList.size}")
            argsList
        } else {
            argsList
        }
    }




    companion object {
        private const val START_CITY_ID = 0
        private const val USER_POSITION = 0
        private const val EMPTY_LIST = 0
        private const val DURATION = 100L

    }
}