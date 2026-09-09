package com.example

import com.example.data.ChannelRepository
import com.example.model.Country
import com.example.model.ProgramItem
import com.example.model.TvCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
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

        // Verify each channel has valid streams and epgAliases
        for (channel in channels) {
            assertTrue("Stream URL must be valid", channel.streamUrl.isNotBlank())
            assertTrue("Channel must have EPG aliases for matching", channel.epgAliases.isNotEmpty())
        }
    }

    @Test
    fun testStrictEpgNoFakeData() {
        val channel = ChannelRepository.getChannels().first()
        // Default channel without real EPG must return null to avoid inventing mock schedules
        assertNull("Channel without real EPG must return null for current program", channel.getCurrentProgram(720))
        assertNull("Channel without real EPG must return null for next program", channel.getNextProgram(720))

        // When populated with real EPG, it must return verified programs
        val verifiedChannel = channel.copy(
            isRealEpg = true,
            schedule = listOf(
                ProgramItem(
                    id = "prog_1",
                    title = "Noticiero Oficial",
                    description = "Información verídica",
                    category = TvCategory.NOTICIAS,
                    startTime = "12:00",
                    endTime = "13:00",
                    startMinutes = 720,
                    endMinutes = 780,
                    isRealEpg = true
                ),
                ProgramItem(
                    id = "prog_2",
                    title = "Documental Cultura",
                    description = "Patrimonio",
                    category = TvCategory.CULTURA,
                    startTime = "13:00",
                    endTime = "14:00",
                    startMinutes = 780,
                    endMinutes = 840,
                    isRealEpg = true
                )
            )
        )

        val current = verifiedChannel.getCurrentProgram(750)
        assertNotNull("Must return current program when real EPG is active", current)
        assertEquals("Noticiero Oficial", current?.title)

        val next = verifiedChannel.getNextProgram(750)
        assertNotNull("Must return next program when real EPG is active", next)
        assertEquals("Documental Cultura", next?.title)
    }
}
