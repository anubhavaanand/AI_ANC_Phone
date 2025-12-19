package com.example.aianc

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.*
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlin.concurrent.thread

class AudioService : Service() {
    private var isRunning = false
    private var monitorMode = false
    private var rnNoise: RNNoise? = null
    private var audioRecord: AudioRecord? = null
    private var audioTrack: AudioTrack? = null

    private val sampleRate = 48000
    private val frameSize = RNNoise.FRAME_SIZE
    private val channelConfigIn = AudioFormat.CHANNEL_IN_MONO
    private val channelConfigOut = AudioFormat.CHANNEL_OUT_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_FLOAT

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopService()
            return START_NOT_STICKY
        }

        monitorMode = intent?.getBooleanExtra(EXTRA_MONITOR_MODE, false) ?: false
        startForegroundService()
        startAudioPipeline()
        return START_STICKY
    }

    private fun startForegroundService() {
        val stopIntent = Intent(this, AudioService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_content))
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, getString(R.string.stop), stopPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun startAudioPipeline() {
        if (isRunning) return
        isRunning = true
        rnNoise = RNNoise()

        val minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfigIn, audioFormat)
        
        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfigIn,
                audioFormat,
                minBufSize.coerceAtLeast(frameSize * 4)
            )

            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build())
                .setAudioFormat(AudioFormat.Builder()
                    .setEncoding(audioFormat)
                    .setSampleRate(sampleRate)
                    .setChannelMask(channelConfigOut)
                    .build())
                .setBufferSizeInBytes(minBufSize.coerceAtLeast(frameSize * 4))
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            thread(name = "AudioProcessingThread") {
                val buffer = FloatArray(frameSize)
                try {
                    audioRecord?.startRecording()
                    if (monitorMode) {
                        audioTrack?.play()
                    }

                    while (isRunning) {
                        val read = audioRecord?.read(buffer, 0, frameSize, AudioRecord.READ_BLOCKING) ?: 0
                        if (read > 0) {
                            rnNoise?.process(buffer)
                            // Only play back if monitor mode is enabled
                            if (monitorMode) {
                                audioTrack?.write(buffer, 0, read, AudioTrack.WRITE_BLOCKING)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error in audio pipeline", e)
                } finally {
                    cleanupResources()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize audio pipeline", e)
            stopService()
        }
    }

    private fun cleanupResources() {
        try {
            audioRecord?.apply {
                if (state == AudioRecord.STATE_INITIALIZED) stop()
                release()
            }
            audioTrack?.apply {
                if (playState == AudioTrack.PLAYSTATE_PLAYING) stop()
                release()
            }
            rnNoise?.release()
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning up resources", e)
        }
        audioRecord = null
        audioTrack = null
        rnNoise = null
    }

    private fun stopService() {
        isRunning = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "AI ANC Service Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Real-time noise cancellation status"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        isRunning = false
        cleanupResources()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val TAG = "AudioService"
        private const val CHANNEL_ID = "AI_ANC_CHANNEL"
        private const val NOTIFICATION_ID = 1
        const val ACTION_STOP = "STOP"
        const val EXTRA_MONITOR_MODE = "monitor_mode"
    }
}
