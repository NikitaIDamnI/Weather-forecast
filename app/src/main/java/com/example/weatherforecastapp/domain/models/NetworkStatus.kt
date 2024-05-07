package com.example.weatherforecastapp.domain.models

import androidx.lifecycle.MutableLiveData

data class NetworkStatus(
    val internetCondition : MutableLiveData<Boolean> = MutableLiveData<Boolean>(true),
    val locationConditionPermission: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false),
    val locationCondition: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true),
)