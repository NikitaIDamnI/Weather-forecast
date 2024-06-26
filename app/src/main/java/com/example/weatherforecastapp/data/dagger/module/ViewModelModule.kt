package com.example.weatherforecastapp.data.dagger.module

import androidx.lifecycle.ViewModel
import com.example.weatherforecastapp.data.dagger.key.ViewModelKey
import com.example.weatherforecastapp.presentation.viewModels.ViewModelWeather
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(ViewModelWeather::class)
    fun bindViewModelAllCities(viewModel: ViewModelWeather): ViewModel



}