package com.example.weatherforecastapp.presentation.checkingСonnections

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PermissionsLauncher(
    private val context: AppCompatActivity
) {
    private lateinit var pLauncher: ActivityResultLauncher<String>


    fun checkPermissions(permission: String) {
        if (!isPermissionGranted(permission)) {

            permissionListener()
            pLauncher.launch(permission)
        }
    }


    private fun permissionListener() {
        pLauncher = context.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            Toast.makeText(context, "Permission is $it", Toast.LENGTH_LONG).show()
            if (it == false) {
                Intent(WeatherReceiver.ACTION_LOCATION).apply {
                    putExtra(WeatherReceiver.CONDITION, it)
                    context.sendBroadcast(this)
                }
            }
        }
    }

    fun isPermissionGranted(permission: String): Boolean {
        val permissionCondition = ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
        Intent(WeatherReceiver.ACTION_LOCATION_PERMISSION).apply {
            putExtra(WeatherReceiver.CONDITION, permissionCondition)
            context.sendBroadcast(this)
        }
        return permissionCondition
    }

    companion object {
        const val PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    }
}