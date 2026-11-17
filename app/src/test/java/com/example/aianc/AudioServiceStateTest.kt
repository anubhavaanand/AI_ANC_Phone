package com.example.aianc

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

/**
 * Unit tests for the process-wide started flag in [AudioService] (issue #13).
 *
 * [AudioService.isServiceRunning] must flip when the service is created and
 * destroyed so MainActivity can show the correct toggle state.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class AudioServiceStateTest {

    @Test
    fun `service is marked running after onCreate`() {
        val controller = Robolectric.buildService(AudioService::class.java)

        controller.create()

        assertTrue(AudioService.isServiceRunning())

        controller.destroy()
    }

    @Test
    fun `service is marked not running after onDestroy`() {
        val controller = Robolectric.buildService(AudioService::class.java)
        controller.create()
        assertTrue(AudioService.isServiceRunning())

        controller.destroy()

        assertFalse(AudioService.isServiceRunning())
    }

    @Test
    fun `ACTION_STOP stops the service without a prior startForeground`() {
        val controller = Robolectric.buildService(AudioService::class.java).create()

        controller
            .withIntent(Intent().setAction(AudioService.ACTION_STOP))
            .startCommand(0, 1)

        val service = controller.get()
        assertTrue(shadowOf(service).isStoppedBySelf)
        assertTrue(shadowOf(service).isForegroundStopped)

        // stopSelf() only schedules destruction; onDestroy() clears the static flag.
        controller.destroy()
        assertFalse(AudioService.isServiceRunning())
    }
}
