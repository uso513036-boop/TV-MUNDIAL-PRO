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
  fun `tvPlayerManager initializes player without error`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val manager = com.example.player.TvPlayerManager(context)
    val player = manager.getPlayer()
    org.junit.Assert.assertNotNull(player)
    manager.release()
  }
}
