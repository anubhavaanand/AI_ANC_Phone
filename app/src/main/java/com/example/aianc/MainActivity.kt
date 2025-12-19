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
    private lateinit var switchMonitorMode: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnToggle = findViewById(R.id.btnToggle)
        tvStatus = findViewById(R.id.tvStatus)
        switchAutoStart = findViewById(R.id.switchAutoStart)
        switchMonitorMode = findViewById(R.id.switchMonitorMode)

        // Load preferences
        val prefs = getSharedPreferences(BootReceiver.PREFS_NAME, Context.MODE_PRIVATE)
        switchAutoStart.isChecked = prefs.getBoolean(BootReceiver.KEY_AUTO_START, false)
        switchMonitorMode.isChecked = prefs.getBoolean(KEY_MONITOR_MODE, false)

        // Auto-start switch listener
        switchAutoStart.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(BootReceiver.KEY_AUTO_START, isChecked).apply()
            val message = if (isChecked) getString(R.string.auto_start_enabled) else getString(R.string.auto_start_disabled)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        // Monitor mode switch listener
        switchMonitorMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(KEY_MONITOR_MODE, isChecked).apply()
            val message = if (isChecked) getString(R.string.monitor_enabled) else getString(R.string.monitor_disabled)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            
            // If service is running, restart it with new mode
            if (isServiceRunning()) {
                stopAudioService()
                android.os.Handler(mainLooper).postDelayed({
                    startAudioService()
                }, 500)
            }
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
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
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
        val prefs = getSharedPreferences(BootReceiver.PREFS_NAME, Context.MODE_PRIVATE)
        val monitorMode = prefs.getBoolean(KEY_MONITOR_MODE, false)
        
        val intent = Intent(this, AudioService::class.java).apply {
            putExtra(AudioService.EXTRA_MONITOR_MODE, monitorMode)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        Toast.makeText(this, getString(R.string.notification_title), Toast.LENGTH_SHORT).show()
        updateUI()
    }

    private fun stopAudioService() {
        val intent = Intent(this, AudioService::class.java).apply {
            action = AudioService.ACTION_STOP
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        Toast.makeText(this, "AI Noise Cancellation Stopped", Toast.LENGTH_SHORT).show()
        updateUI()
    }

    private fun updateUI() {
        val running = isServiceRunning()
        if (running) {
            btnToggle.text = getString(R.string.stop_anc)
            tvStatus.text = getString(R.string.status_active)
            tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        } else {
            btnToggle.text = getString(R.string.start_anc)
            tvStatus.text = getString(R.string.status_inactive)
            tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startAudioService()
            } else {
                Toast.makeText(this, getString(R.string.perm_required), Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private const val KEY_MONITOR_MODE = "monitor_mode_enabled"
    }
}
