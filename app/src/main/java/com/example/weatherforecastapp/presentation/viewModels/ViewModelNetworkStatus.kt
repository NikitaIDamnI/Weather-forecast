package com.example.weatherforecastapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.example.weatherforecastapp.domain.models.NetworkStatus
import javax.inject.Inject

class ViewModelNetworkStatus @Inject constructor() : ViewModel() {

    val networkStatus = NetworkStatus()

    fun checkLocationCondition(condition: Boolean) {
        networkStatus.locationCondition.value = condition
    }
    fun checkLocationStatusPermission(condition: Boolean) {
        networkStatus.locationConditionPermission.value = condition
    }

    fun checkInternetCondition(condition: Boolean) {
        networkStatus.internetCondition.value = condition
    }


}