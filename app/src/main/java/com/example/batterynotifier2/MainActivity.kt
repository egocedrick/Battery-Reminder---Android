package com.example.batterynotifier2

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.toyomansi.batteryreminder.R

class MainActivity : AppCompatActivity() {

    private lateinit var rootLayout: android.widget.LinearLayout
    private lateinit var percentText: TextView
    private lateinit var statusText: TextView

    private var uiReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val batteryPct = (level / scale.toFloat() * 100).toInt()

            updateUi(batteryPct)
        }
    }

    @SuppressLint("BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootLayout = findViewById(R.id.rootLayout)
        percentText = findViewById(R.id.batteryPercentText)
        statusText = findViewById(R.id.statusMessageText)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }

            startForegroundService(Intent(this, BatteryService::class.java))

            val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = "package:$packageName".toUri()
                }
                startActivity(intent)
            }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(uiReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(uiReceiver)
    }
    @SuppressLint("SetTextI18n", "UseKtx")
    private fun updateUi(batteryPct: Int) {
        percentText.text = "$batteryPct%"

    when {
        batteryPct <= 5 -> {
            rootLayout.setBackgroundColor(Color.parseColor("#B71C1C")) // dark red
            statusText.text = "⚠️ DANGER ZONE\nCharge immediately."
        }
        batteryPct <= 20 -> {
            rootLayout.setBackgroundColor(Color.parseColor("#F9A825")) // amber
            statusText.text = "Battery Warning\nPlease charge soon."
        }
        else -> {
            rootLayout.setBackgroundColor(Color.parseColor("#2E7D32")) // green
            statusText.text = "Battery Normal\nMonitoring in background."
        }
    }

    }
}