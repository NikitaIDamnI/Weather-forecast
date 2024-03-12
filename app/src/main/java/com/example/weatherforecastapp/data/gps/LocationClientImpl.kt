package com.example.weatherforecastapp.data.gps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseSaveUserLocation
import com.example.weatherforecastapp.domain.repositoryLocation.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class LocationClientImpl @Inject constructor(
    private val appActivity: AppCompatActivity,
    private val saveToDB: UseCaseSaveUserLocation,
    private var fLocationClient: FusedLocationProviderClient,
    private val pPermissionsLauncher: PermissionsLauncher
) : LocationRepository {



    private fun getLocation() {
        pPermissionsLauncher
        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                appActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                appActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener {
                val loc = "${it.result.latitude},${it.result.longitude}"
                CoroutineScope(Dispatchers.IO).launch {
                    saveToDB(loc)
                }
                Log.d("My_Log", "${it.result.latitude},${it.result.longitude}")
            }

    }

    override fun checkLocation() {
        if (isLocationEnabled()) {
            getLocation()
        } else {
            DialogManager.locationSettingsDialog(appActivity, object : DialogManager.Listener {
                override fun onClick() {
                    appActivity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
        }
    }

    private fun isLocationEnabled(): Boolean {
        val lm = appActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


}








