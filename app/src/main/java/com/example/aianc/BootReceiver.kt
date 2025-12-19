package com.example.aianc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

/**
 * Broadcast receiver that automatically starts the AI ANC service
 * when the device boots up (if the service was running before).
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "Boot completed, starting AI ANC service")
            
            // Check if auto-start is enabled in preferences
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val autoStart = prefs.getBoolean(KEY_AUTO_START, false)
            
            if (autoStart) {
                val serviceIntent = Intent(context, AudioService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }
                Log.d(TAG, "AI ANC service started on boot")
            }
        }
    }

    companion object {
        private const val TAG = "BootReceiver"
        const val PREFS_NAME = "ai_anc_prefs"
        const val KEY_AUTO_START = "auto_start_enabled"
    }
}
