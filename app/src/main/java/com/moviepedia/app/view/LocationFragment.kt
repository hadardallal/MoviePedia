package com.moviepedia.app.view

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviepedia.app.R
import com.moviepedia.app.databinding.FragmentLocationBinding
import com.moviepedia.app.services.LocationService
import com.moviepedia.app.utils.PermissionUtil


class LocationFragment : Fragment() {
    private lateinit var binding:FragmentLocationBinding

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val latitude = intent.getDoubleExtra("latitude", 0.0)
            val longitude = intent.getDoubleExtra("longitude", 0.0)
            binding.locationTextView.text = "${R.string.latitude} $latitude, ${R.string.longitude} $longitude"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentLocationBinding.inflate(inflater, container, false)

        binding.getLocationButton.setOnClickListener {
            requestLocationPermission()
        }
    return binding.root
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("LocationUpdate")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().registerReceiver(locationReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        }
        else{
            requireActivity().registerReceiver(locationReceiver, filter)
        }
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(locationReceiver)
    }

    private fun requestLocationPermission() {
        if (PermissionUtil.hasLocationPermission(requireActivity())) {
            startLocationService()
        } else {

            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            startLocationService()
        } else {
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.permission_alert_message)
                .setMessage(R.string.location_permission_denied_message)
                .setCancelable(false)
                .setPositiveButton(R.string.setting){ dialog,which->
                    dialog.dismiss()
                    PermissionUtil.openAppSettings(requireActivity())
                }
                .setNegativeButton(R.string.cancel){dialog,which->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun startLocationService() {
        val locationServiceIntent = Intent(requireContext(), LocationService::class.java)
        requireActivity().startService(locationServiceIntent)
    }
}