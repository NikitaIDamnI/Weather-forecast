package com.example.weatherforecastapp.data.dagger.module

import androidx.lifecycle.ViewModel
import com.example.weatherforecastapp.data.dagger.key.ViewModelKey
import com.example.weatherforecastapp.presentation.viewModels.ViewModelAllCities
import com.example.weatherforecastapp.presentation.viewModels.ViewModelNetworkStatus
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelWeather::class)
    fun bindViewModelWeather(viewModel: ViewModelWeather): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelAllCities::class)
    fun bindViewModelAllCities(viewModel: ViewModelAllCities): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelNetworkStatus::class)
    fun bindViewModelNetworkStatus(viewModel: ViewModelNetworkStatus): ViewModel


}