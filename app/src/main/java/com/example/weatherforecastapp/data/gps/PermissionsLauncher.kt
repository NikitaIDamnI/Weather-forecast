package com.example.weatherforecastapp.data.gps

import android.Manifest
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject

class PermissionsLauncher @Inject constructor(
    private val context: AppCompatActivity
) {
    private lateinit var pLauncher: ActivityResultLauncher<String>


    init {
        checkPermissions()
    }


    private fun checkPermissions() {
        if (!context.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            permissionListener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun permissionListener() {
        pLauncher = context.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            Toast.makeText(context, "Permission is $it", Toast.LENGTH_LONG).show()
        }
    }

}