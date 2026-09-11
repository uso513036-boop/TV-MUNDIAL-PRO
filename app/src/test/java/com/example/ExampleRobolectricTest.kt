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

  @Test
  fun `pbo tv has authentic epg schedule and no willax alias`() {
    val channels = com.example.data.ChannelRepository.getChannels()
    val pbo = channels.find { it.id == "pe_pbo_tv" }
    org.junit.Assert.assertNotNull("PBO TV channel must exist", pbo)
    org.junit.Assert.assertTrue("PBO must have correct aliases", pbo!!.epgAliases.contains("PBOTV.pe"))
    org.junit.Assert.assertTrue("PBO must not contain Willax alias", !pbo.epgAliases.contains("Willax"))

    val context = ApplicationProvider.getApplicationContext<Context>()
    val epgRepo = com.example.data.EpgRepository(context)
    val schedule = epgRepo.generateOfficialPboSchedule(com.example.model.Country.PERU.timeZone)
    org.junit.Assert.assertTrue("PBO schedule should not be empty", schedule.isNotEmpty())

    val titles = schedule.map { it.title }
    org.junit.Assert.assertTrue("Must include PBO Noticias", titles.any { it.contains("PBO Noticias") })
    org.junit.Assert.assertTrue("Must include Chema Salcedo", titles.any { it.contains("Chema Salcedo") })
    org.junit.Assert.assertTrue("Must include PBO Campeonísimo", titles.any { it.contains("Campeonísimo") })
    org.junit.Assert.assertTrue("Must include PBO Salud", titles.any { it.contains("PBO Salud") })
  }

  @Test
  fun `vm latino channel has authentic epg schedule`() {
    val channels = com.example.data.ChannelRepository.getChannels()
    val vmLatino = channels.find { it.id == "cr_vm_latino" }
    org.junit.Assert.assertNotNull("VM Latino channel must exist", vmLatino)
    org.junit.Assert.assertEquals("VM Latino stream must be intact", "https://59ef525c24caa.streamlock.net/vmtv/vmlatino/playlist.m3u8", vmLatino!!.streamUrl)

    val context = ApplicationProvider.getApplicationContext<Context>()
    val epgRepo = com.example.data.EpgRepository(context)
    val schedule = epgRepo.generateOfficialVmLatinoSchedule(com.example.model.Country.COSTA_RICA.timeZone)
    org.junit.Assert.assertTrue("VM Latino schedule should not be empty", schedule.isNotEmpty())

    val titles = schedule.map { it.title }
    org.junit.Assert.assertTrue("Must include La Dosis or A la Kma Con", titles.any { it.contains("La Dosis") || it.contains("A la Kma Con") })
    org.junit.Assert.assertTrue("Must include Top 10 or Top 20", titles.any { it.contains("Top 10") || it.contains("Top 20") })
    org.junit.Assert.assertTrue("Must include Zona Urbana", titles.any { it.contains("Zona Urbana") })
    org.junit.Assert.assertTrue("Must include VM Retro", titles.any { it.contains("VM Retro") })
  }
}
