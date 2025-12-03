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
import android.os.Build
import android.os.IBinder
import android.app.admin.DevicePolicyManager
import android.util.Log
import androidx.core.app.NotificationCompat

class BatteryService : Service() {

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val batteryPct = (level / scale.toFloat() * 100).toInt()

            Log.d("BatteryService", "Battery level: $batteryPct%")

            when (batteryPct) {
                20 -> {
                    showNotification("Battery low (20%)", "Please charge soon.")
                }
                5 -> {
                    showNotification("Battery Critical (5%)", "Device will lock.")

                    val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                    val componentName = ComponentName(this@BatteryService, MyDeviceAdminReceiver::class.java)

                    if (dpm.isAdminActive(componentName)) {
                        dpm.lockNow() // 🔒 lock device immediately
                    } else {
                        showNotification("Admin not active", "Cannot lock device. Please enable Device Admin.")
                        Log.e("BatteryService", "Device Admin not active, lockNow() skipped.")
                    }
                }
            }
        }
    }

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

        // ✅ Register continuous battery monitoring
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
    private fun showNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(this, "battery_service")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(title.hashCode(), notification)
    }
}