package com.example.aianc

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val permissions = mutableListOf(
        Manifest.permission.RECORD_AUDIO
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(Manifest.permission.BLUETOOTH_CONNECT)
        }
    }

    private lateinit var btnToggle: Button
    private lateinit var tvStatus: TextView
    private lateinit var switchAutoStart: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnToggle = findViewById(R.id.btnToggle)
        tvStatus = findViewById(R.id.tvStatus)
        switchAutoStart = findViewById(R.id.switchAutoStart)

        // Load auto-start preference
        val prefs = getSharedPreferences(BootReceiver.PREFS_NAME, Context.MODE_PRIVATE)
        switchAutoStart.isChecked = prefs.getBoolean(BootReceiver.KEY_AUTO_START, false)

        // Auto-start switch listener
        switchAutoStart.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(BootReceiver.KEY_AUTO_START, isChecked).apply()
            val message = if (isChecked) "Auto-start enabled" else "Auto-start disabled"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        btnToggle.setOnClickListener {
            if (checkPermissions()) {
                if (isServiceRunning()) {
                    stopAudioService()
                } else {
                    startAudioService()
                }
            } else {
                requestPermissions()
            }
        }

        updateUI()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun checkPermissions(): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 100)
    }

    private fun isServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        @Suppress("DEPRECATION")
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AudioService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun startAudioService() {
        val intent = Intent(this, AudioService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        Toast.makeText(this, "AI Noise Cancellation Started", Toast.LENGTH_SHORT).show()
        updateUI()
    }

    private fun stopAudioService() {
        val intent = Intent(this, AudioService::class.java).apply {
            action = "STOP"
        }
        startService(intent)
        Toast.makeText(this, "AI Noise Cancellation Stopped", Toast.LENGTH_SHORT).show()
        updateUI()
    }

    private fun updateUI() {
        val running = isServiceRunning()
        if (running) {
            btnToggle.text = "Stop AI ANC"
            tvStatus.text = "Status: Active ✓"
            tvStatus.setTextColor(getColor(android.R.color.holo_green_dark))
        } else {
            btnToggle.text = "Start AI ANC"
            tvStatus.text = "Status: Inactive"
            tvStatus.setTextColor(getColor(android.R.color.holo_red_dark))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            startAudioService()
        } else {
            Toast.makeText(this, "Permissions required for AI ANC", Toast.LENGTH_LONG).show()
        }
    }
}

