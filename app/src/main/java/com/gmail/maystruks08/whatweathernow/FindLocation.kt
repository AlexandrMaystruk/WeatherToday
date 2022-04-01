package com.gmail.maystruks08.whatweathernow

import android.content.Context
import android.os.Bundle
import android.location.LocationManager
import android.location.LocationListener
import android.location.Location
import android.util.Log
import android.content.Intent
import android.net.Uri
import android.provider.Settings


class FindLocation(val context: Context) {

    interface MyLocationChange {
        fun callbackLocationChange(loc: Location)
    }

    private var callback: MyLocationChange? = null
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    private val locationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            turnGPSOff()
            stopLocationMonitoring()
            callback?.callbackLocationChange(location)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.d("find", provider + "nStatusChanged------------------------>")
        }

        override fun onProviderEnabled(provider: String) {
            Log.d("find", provider + "onProviderEnabled------------------------>")
        }

        override fun onProviderDisabled(provider: String) {
            Log.d("find", provider + "onProviderDisabled------------------------>")
        }
    }

    fun addLocationListener(listener: MyLocationChange) {
        this.callback = listener
    }

    fun startLocationMonitoring() {

        turnGPSOn()

        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                callback?.callbackLocationChange(it)
            }
        } catch (e: SecurityException) {
            Log.d(e.localizedMessage, e.stackTrace.toString())
        }
    }

    fun turnGPSOff() {
        Log.d("find", "turnGPSOff------------------------>")
        val provider = Settings.Secure.getString(context.contentResolver, Settings.Secure.ALLOWED_GEOLOCATION_ORIGINS)
        if (provider.contains("gps")) { //if gps is enabled
            val poke = Intent()
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider")
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
            poke.data = Uri.parse("3")
            context.sendBroadcast(poke)
        }
    }

    fun turnGPSOn() {
        val provider = Settings.Secure.getString(context.contentResolver, Settings.Secure.ALLOWED_GEOLOCATION_ORIGINS)

        if (!provider.contains("gps")) { //if gps is disabled
            val poke = Intent()
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider")
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
            poke.data = Uri.parse("3")
            context.sendBroadcast(poke)
        }
        Log.d("find", "turnGPSOn------------------------>")
    }

    fun stopLocationMonitoring() {
        locationManager.removeUpdates(locationListener)
        turnGPSOff()
        Log.d("find", "stopLocationMonitoring------------------------>")

    }

}
