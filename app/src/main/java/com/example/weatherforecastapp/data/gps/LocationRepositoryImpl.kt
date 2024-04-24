package com.example.weatherforecastapp.data.gps

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.weatherforecastapp.data.Format
import com.example.weatherforecastapp.data.database.models.PositionDb
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseGetUserLocation
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseSaveUserLocation
import com.example.weatherforecastapp.domain.repositoryLocation.LocationRepository
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
    private val getUserLocation: UseCaseGetUserLocation,
) : LocationRepository {
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
                CoroutineScope(Dispatchers.IO).launch {
                    val locationDb = getUserLocation.invoke()
                    Log.d("LocationRepositoryImpl", "locationDb: $locationDb ")
                    val positionGPS = dataRounding(it)
                    if (locationDb.position != positionGPS.position) {
                        Log.d("LocationRepositoryImpl", "Update: true ")
                        saveUserLocation(dataRounding(it))

                    } else {
                        Log.d("LocationRepositoryImpl", "locationDb: false ")
                    }
                }

            }
    }

    override fun checkLocation(context: Context) {
        val isLocationEnabled = isLocationEnabled()
        if (isLocationEnabled) {
            Log.d("LocationRepositoryImpl", "isLocationEnabled:$isLocationEnabled ")
            getLocation()
        } else {
            DialogManager.locationSettingsDialog(context, object : DialogManager.Listener {
                override fun onClickPositive() {
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }

                override fun onClickNegative() {

                }
            })
        }
    }


    private fun isLocationEnabled(): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun dataRounding(location: Task<Location>): PositionDb {
        val latitude = "%.2f".format(location.result.latitude).toDouble()
        val longitude = "%.2f".format(location.result.longitude).toDouble()
        val time = System.currentTimeMillis()
        Log.d("My_Log", "$latitude,$longitude")

        return PositionDb(
            RepositoryDataImpl.CURRENT_LOCATION_ID,
            "$latitude,$longitude",
            Format.formatTimeFromEpoch(time)
        )
    }


}








