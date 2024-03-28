package com.example.weatherforecastapp.domain.repositoryLocation

interface LocationRepository {

    fun checkLocation()
    fun setUpdateListener(listener: LocationUpdateListener)


    interface LocationUpdateListener {
        fun onUpdate()
    }

}
