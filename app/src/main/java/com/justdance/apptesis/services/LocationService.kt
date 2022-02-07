package com.justdance.apptesis.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.coroutineContext

class LocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return /*START_STICKY*/ START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        /*fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createLocationRequest()
        createLocationCallBack()*/
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()
            .setInterval(10000)
            .setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        settingsClient = LocationServices.getSettingsClient(this)

        val task: Task<LocationSettingsResponse> = settingsClient.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@addOnSuccessListener
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    //exception.startResolutionForResult(this@MainActivity,
                    //REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun createLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                var address: Address? = null

                if (locationResult.locations.isEmpty())
                    return

                val location = locationResult.locations.first()

                CoroutineScope(Dispatchers.IO).launch {
                    runCatching {
                        val geoCode = Geocoder(applicationContext, Locale.getDefault())
                        val addressList = geoCode.getFromLocation(location.latitude, location.longitude, 1)
                        address = addressList.firstOrNull()
                    }
                }.invokeOnCompletion {
                    if (address == null) {
                        Log.d("Address calculation", "Address was null.")
                        return@invokeOnCompletion
                    }

                    val sb = StringBuilder()
                    for (i in 0 until address!!.maxAddressLineIndex) {
                        sb.append(address!!.getAddressLine(i)).append(" ")
                    }
                    sb.append(address!!.locality).append(" ")
                    sb.append(address!!.subLocality).append(" ")
                    sb.append(address!!.thoroughfare).append(" ")
                    sb.append(address!!.subThoroughfare).append(" ")
                    sb.append(address!!.adminArea).append(" ")
                    sb.append(address!!.subAdminArea).append(" ")
                    sb.append(address!!.premises).append(" ")
                    sb.append(address!!.featureName)
                    sb.append(address!!.postalCode).append(" ")
                    sb.append(address!!.countryName)
                    val result = sb.toString()
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(
                            applicationContext,
                            result,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        //fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
