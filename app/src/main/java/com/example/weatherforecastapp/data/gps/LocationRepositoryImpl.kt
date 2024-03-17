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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherforecastapp.data.database.models.Position
import com.example.weatherforecastapp.data.repository.RepositoryDataImpl
import com.example.weatherforecastapp.domain.repisitoryData.UseCase.UseCaseSaveUserLocation
import com.example.weatherforecastapp.domain.repositoryLocation.LocationRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LocationRepositoryImpl (
    private val appActivity: AppCompatActivity,
    context: Application,
    ) : LocationRepository {
    private val repositoryImpl = RepositoryDataImpl(context)
    private val saveToDB = UseCaseSaveUserLocation(repositoryImpl)
    private val pPermissionsLauncher =  PermissionsLauncher(appActivity)
    private var fLocationClient = LocationServices.getFusedLocationProviderClient(appActivity)




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
                CoroutineScope(Dispatchers.IO).launch {
                    saveToDB(dataRounding(it))
                }
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

   private fun dataRounding(location:Task<Location>): Position{
       val latitude = "%.2f".format( location.result.latitude).toDouble()
       val longitude = "%.2f".format(location.result.longitude).toDouble()
       val time = System.currentTimeMillis() / 1000
       Log.d("My_Log", "$latitude,$longitude")

       return Position(RepositoryDataImpl.CURRENT_LOCATION_ID,"$latitude,$longitude",time)
   }


}








