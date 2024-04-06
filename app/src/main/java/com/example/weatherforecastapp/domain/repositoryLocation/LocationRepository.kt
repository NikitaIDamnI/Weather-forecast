package com.example.weatherforecastapp.domain.repositoryLocation

import android.content.Context

interface LocationRepository {

    fun checkLocation(context: Context)
}
