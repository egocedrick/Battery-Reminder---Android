package com.example.batterynotifier2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class InstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return

        if (action == Intent.ACTION_MY_PACKAGE_REPLACED) {

            context?.let {
                val svc = Intent(it, BatteryService::class.java)
                it.startForegroundService(svc)
            }
        }
    }
}
