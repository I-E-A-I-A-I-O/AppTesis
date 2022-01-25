package com.justdance.apptesis.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast

private class MyBinder(private val servc: LocationService): Binder() {
    fun getService(): LocationService {
        return servc
    }
}

class LocationService : Service() {
    private val binder: IBinder = MyBinder(this)

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        onTaskRemoved(intent)
        Toast.makeText(
            applicationContext, "This is a Service running in Background",
            Toast.LENGTH_SHORT
        ).show()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)
        startService(restartServiceIntent)
        super.onTaskRemoved(rootIntent)
    }
}
