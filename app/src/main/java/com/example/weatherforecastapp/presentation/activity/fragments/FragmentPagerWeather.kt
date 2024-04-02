package com.example.weatherforecastapp.presentation.activity.fragments

import PagerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.databinding.FragmentPagerWeatherBinding
import com.example.weatherforecastapp.presentation.viewModels.ViewModelFactory
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import com.google.android.material.tabs.TabLayoutMediator


class FragmentPagerWeather : Fragment() {

    private var _binding: FragmentPagerWeatherBinding? = null
    private val binding: FragmentPagerWeatherBinding
        get() = _binding ?: throw RuntimeException("FragmentPagerWeatherBinding = null")

    private val args by navArgs<FragmentPagerWeatherArgs>()

    private val viewModelFactory by lazy {
        ViewModelFactory(requireActivity().application, args.id)
    }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ViewModelWeather::class.java]
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
        initial()
    }

    private fun initial() {
        animationToolbar()
        binding.bMenu.setOnClickListener {
            val action = FragmentPagerWeatherDirections.actionFragmentPagerWeatherToFragmentAllCities()
            findNavController().navigate(action)


        }
        viewModel.sizeCity.observe(viewLifecycleOwner) {
            val argsList = getListArgs(it)
            Log.d("FragmentPagerWeather_Log", "sizeCity: $it")
            val pager = PagerAdapter(requireActivity(),args.id,argsList)

            with(binding) {

                viewPager.adapter = pager
                viewPager.setCurrentItem(args.id, false,)

                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                    when (position) {
                        0 -> {
                            tab.setIcon(R.drawable.ic_nav)
                        }

                        else -> {
                            tab.setIcon(R.drawable.ic_tochka)
                        }
                    }
                }.attach()

                if (it == 0) {
                    viewPager.visibility = View.GONE

                }

                viewPager.visibility = View.VISIBLE
            }
        }

    }

    private fun animationToolbar() = with(binding) {
        cardToolbar.alpha = 0f
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                root.visibility = View.VISIBLE
                cardToolbar.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .start()
                load.animate()
                    .alpha(0f)
                    .setDuration(500)
                    .start()
            }
        })
    }


    private fun getListArgs(idCity: Int): List<Bundle> {
        val argsList = mutableListOf<Bundle>()
        if (idCity != 0) {
            for (id in START_CITY_ID..<idCity) {
                val args = Bundle().apply {
                    putInt("id", id)
                }
                argsList.add(args)
            }
            Log.d("FragmentPagerWeather", "argsList.size: ${argsList.size}")
            return argsList
        } else {
            return argsList
        }

    }


    companion object {
        private const val START_CITY_ID = 0


    }
}