package com.example.weatherforecastapp.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseGetLocations
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseSearchCity
import kotlinx.coroutines.launch

class ViewModelAllCities(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RepositoryDataImpl(application)
    private val useCaseGetLocations = UseCaseGetLocations(repository)
    private val useCaseSearchCity = UseCaseSearchCity(repository)

    fun searchCity(city: String): List<SearchCity> {
        viewModelScope.launch {
        }
        TODO()
    }

}