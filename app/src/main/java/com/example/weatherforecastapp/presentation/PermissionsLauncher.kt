package com.example.weatherforecastapp.presentation

import android.Manifest
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
        }
    }

     fun isPermissionGranted(permission: String): Boolean {
         val permissionCondition = ContextCompat.checkSelfPermission(
             context,
             permission
         ) == PackageManager.PERMISSION_GRANTED
       return permissionCondition
    }

    companion object {
        const val PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    }
}