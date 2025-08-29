package com.example.batterynotifier2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class BatteryService : Service() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "battery_service",
                "Battery Service",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, "battery_service")
            .setContentTitle("Battery Service Running")
            .setContentText("Monitoring battery in background")
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
            .build()

        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Your battery monitoring logic here...
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
