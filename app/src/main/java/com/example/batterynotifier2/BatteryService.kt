package com.example.batterynotifier2

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.IBinder
import android.app.admin.DevicePolicyManager
import android.util.Log
import androidx.core.app.NotificationCompat

class BatteryService : Service() {

    companion object {
        const val FOREGROUND_ID = 1000
        const val NOTIF_20 = 2000
        const val NOTIF_5 = 3000
    }

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val batteryPct = (level / scale.toFloat() * 100).toInt()

            Log.d("BatteryService", "Battery level: $batteryPct%")

            when {
                batteryPct <= 5 -> {
                    showNotification(NOTIF_5,"Battery Critical (5%)", "Device will lock.")

                    val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                    val componentName = ComponentName(this@BatteryService, MyDeviceAdminReceiver::class.java)

                    if (dpm.isAdminActive(componentName)) {
                        dpm.lockNow()
                    } else {
                        showNotification(NOTIF_5, "Admin not active", "Cannot lock device. Please enable admin.")
                        Log.e("BatteryService", "Device Admin not active, lockNow() skipped.")
                    }
                }
                batteryPct <= 20 -> {
                    showNotification(NOTIF_20,"Battery low (20%)", "Please charge soon.")
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
            val foregroundChannel = NotificationChannel(
                "battery_service",
                "Battery Service",
                NotificationManager.IMPORTANCE_LOW
            )

            val alertChannel = NotificationChannel(
                "battery_alerts",
                "Battery Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(foregroundChannel)
            manager.createNotificationChannel(alertChannel)

        val notification: Notification = NotificationCompat.Builder(this, "battery_service")
            .setContentTitle("Battery Service Running")
            .setContentText("Monitoring battery in background")
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
            .build()

        startForeground(FOREGROUND_ID, notification)

        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("NotificationPermission")
    private fun showNotification(id: Int = 0 ,title: String, message: String) {
        val notification = NotificationCompat.Builder(this, "battery_alerts")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(id, notification)
    }
}