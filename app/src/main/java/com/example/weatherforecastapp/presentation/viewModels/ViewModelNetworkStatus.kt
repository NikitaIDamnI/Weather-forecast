package com.example.weatherforecastapp.presentation.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecastapp.presentation.checkingСonnections.State
import javax.inject.Inject

class ViewModelNetworkStatus @Inject constructor() : ViewModel() {

    val state = MutableLiveData<State>()

    fun getState(newState: State){
        state.value = newState
        Log.d("ViewModelNetworkStatus", "state: $newState")
    }



}