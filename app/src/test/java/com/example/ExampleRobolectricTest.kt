package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("TV MUNDIAL PRO", appName)
  }

  @Test
  fun `launch MainActivity`() {
    androidx.test.core.app.ActivityScenario.launch(MainActivity::class.java).use { scenario ->
      scenario.onActivity { activity ->
        org.junit.Assert.assertNotNull(activity)
      }
    }
  }

  @Test
  fun `tvPlayerManager initializes player without error`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val manager = com.example.player.TvPlayerManager(context)
    val player = manager.getPlayer()
    org.junit.Assert.assertNotNull(player)
    manager.release()
  }
}
