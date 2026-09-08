package com.example

import com.example.data.ChannelRepository
import com.example.model.Country
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ChannelRepositoryTest {

    @Test
    fun testChannelsLoadedForPeruAndCostaRica() {
        val channels = ChannelRepository.getChannels()
        assertTrue("Channels should not be empty", channels.isNotEmpty())

        val peruChannels = channels.filter { it.country == Country.PERU }
        val costaRicaChannels = channels.filter { it.country == Country.COSTA_RICA }

        assertTrue("Should have multiple Peru channels", peruChannels.size >= 5)
        assertTrue("Should have multiple Costa Rica channels", costaRicaChannels.size >= 5)

        // Verify each channel has valid streams and non-empty EPG
        for (channel in channels) {
            assertTrue("Stream URL must be valid", channel.streamUrl.isNotBlank())
            assertTrue("EPG schedule must not be empty", channel.schedule.isNotEmpty())
            assertNotNull("Channel must have a current program", channel.getCurrentProgram(720))
        }
    }
}
