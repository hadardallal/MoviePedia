package com.moviepedia.app.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat

object PermissionUtil {

    private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    private const val COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    private const val FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION

    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED
    }

    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, COARSE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, FINE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED
    }

    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

}
