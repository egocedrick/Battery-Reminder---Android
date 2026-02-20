package com.example.batterynotifier2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class InstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return

        if (action == Intent.ACTION_MY_PACKAGE_REPLACED ||
            action == Intent.ACTION_PACKAGE_ADDED) {

            context?.let {
                val svc = Intent(it, BatteryService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.startForegroundService(svc)
                } else {
                    it.startService(svc)
                }
            }
        } else {
            Log.d("InstallReceiver", "Ignored action: $action")
        }
    }
}
