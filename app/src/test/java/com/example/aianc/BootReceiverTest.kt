package com.example.aianc

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

/**
 * Unit tests for [BootReceiver] auto-start behavior on device boot.
 *
 * Verifies that the AI ANC service is only started when the user has
 * explicitly enabled auto-start in preferences (issue #19).
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class BootReceiverTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        // Reset preferences between tests; commit() for synchronous writes.
        context.getSharedPreferences(BootReceiver.PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
    }

    private fun nextStartedService(): Intent? =
        shadowOf(context as ContextWrapper).nextStartedService

    @Test
    fun `onReceive with auto-start enabled starts the audio service`() {
        context.getSharedPreferences(BootReceiver.PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(BootReceiver.KEY_AUTO_START, true)
            .commit()

        BootReceiver().onReceive(context, Intent(Intent.ACTION_BOOT_COMPLETED))

        val started = nextStartedService()
        assertEquals(AudioService::class.java.name, started?.component?.className)
    }

    @Test
    fun `onReceive with auto-start disabled does not start any service`() {
        context.getSharedPreferences(BootReceiver.PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(BootReceiver.KEY_AUTO_START, false)
            .commit()

        BootReceiver().onReceive(context, Intent(Intent.ACTION_BOOT_COMPLETED))

        assertNull(nextStartedService())
    }

    @Test
    fun `onReceive ignores intents that are not boot completed`() {
        context.getSharedPreferences(BootReceiver.PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(BootReceiver.KEY_AUTO_START, true)
            .commit()

        BootReceiver().onReceive(context, Intent(Intent.ACTION_TIME_CHANGED))

        assertNull(nextStartedService())
    }

    @Test
    fun `onReceive with no stored preference does not start any service`() {
        // Default behavior: auto-start off when the user never toggled it.
        BootReceiver().onReceive(context, Intent(Intent.ACTION_BOOT_COMPLETED))

        assertNull(nextStartedService())
    }
}
