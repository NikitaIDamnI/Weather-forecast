package com.example.weatherforecastapp.data.gps

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.weatherforecastapp.data.Format
import com.example.weatherforecastapp.data.database.models.PositionDb
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseSaveUserLocation
import com.example.weatherforecastapp.domain.repositoryLocation.LocationRepository
import com.example.weatherforecastapp.presentation.checkingСonnections.WeatherReceiver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class LocationRepositoryImpl @Inject constructor(
    private val context: Application,
    private val saveUserLocation: UseCaseSaveUserLocation,
    private val fLocationClient: FusedLocationProviderClient,
) : LocationRepository {
    val coroutineScopeGPS = CoroutineScope(Dispatchers.IO)
    private fun getLocation() {
        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener {
                coroutineScopeGPS.launch {
                    saveUserLocation(dataRounding(it))
                }
            }
    }

    override fun checkLocation(context: Context) {
        val isLocationEnabled = isLocationEnabled()
        if (isLocationEnabled) {
            Log.d("LocationRepositoryImpl", "isLocationEnabled:$isLocationEnabled ")
            getLocation()
        }
    }


    private fun isLocationEnabled(): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Intent(WeatherReceiver.ACTION_LOCATION).apply {
            putExtra(WeatherReceiver.CONDITION, locationEnabled)
            context.sendBroadcast(this)
        }
        return locationEnabled
    }

    private fun dataRounding(location: Task<Location>): PositionDb {
        val latitude = "%.2f".format(location.result.latitude).toDouble()
        val longitude = "%.2f".format(location.result.longitude).toDouble()
        val time = System.currentTimeMillis()
        Log.d("My_Log", "$latitude,$longitude")

        return PositionDb(
            RepositoryDataImpl.USER_ID,
            "$latitude,$longitude",
            Format.formatTimeFromEpoch(time)
        )
    }


}








