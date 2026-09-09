package com.example.model

enum class Country(
    val code: String,
    val displayName: String,
    val flag: String,
    val timeZone: String
) {
    PERU("PE", "Perú", "🇵🇪", "America/Lima"),
    COSTA_RICA("CR", "Costa Rica", "🇨🇷", "America/Costa_Rica")
}

enum class TvCategory(val displayName: String, val iconName: String) {
    TODOS("Todos", "apps"),
    NOTICIAS("Noticias", "newspaper"),
    ENTRETENIMIENTO("Entretenimiento", "movie"),
    DEPORTES("Deportes", "sports_soccer"),
    CULTURA("Cultura", "school"),
    MUSICA("Música", "music_note"),
    INFANTIL("Infantil", "child_care")
}

data class ProgramItem(
    val id: String,
    val title: String,
    val description: String,
    val category: TvCategory,
    val startTime: String, // format "HH:mm" e.g. "07:00"
    val endTime: String,   // format "HH:mm" e.g. "08:30"
    val startMinutes: Int, // minutes from 00:00 (e.g. 7 * 60 = 420)
    val endMinutes: Int,   // minutes from 00:00
    val rating: String = "TP", // "TP", "+14", "+18"
    val hostOrStar: String = "",
    val isReminderSet: Boolean = false,
    val epochStartMs: Long = 0L,
    val epochEndMs: Long = 0L,
    val isRealEpg: Boolean = false
) {
    fun isCurrentlyAiring(currentMinutes: Int, currentEpochMs: Long = System.currentTimeMillis()): Boolean {
        if (epochStartMs > 0L && epochEndMs > 0L) {
            return currentEpochMs in epochStartMs until epochEndMs
        }
        return if (endMinutes > startMinutes) {
            currentMinutes in startMinutes until endMinutes
        } else {
            // Over midnight
            currentMinutes >= startMinutes || currentMinutes < endMinutes
        }
    }

    fun getProgressPercent(currentMinutes: Int, currentEpochMs: Long = System.currentTimeMillis()): Float {
        if (epochStartMs > 0L && epochEndMs > 0L) {
            val duration = epochEndMs - epochStartMs
            if (duration <= 0L) return 0f
            return ((currentEpochMs - epochStartMs).toFloat() / duration.toFloat()).coerceIn(0f, 1f)
        }
        val total = if (endMinutes > startMinutes) {
            endMinutes - startMinutes
        } else {
            (24 * 60 - startMinutes) + endMinutes
        }
        if (total <= 0) return 0f

        val elapsed = if (endMinutes > startMinutes) {
            (currentMinutes - startMinutes).coerceIn(0, total)
        } else {
            if (currentMinutes >= startMinutes) {
                (currentMinutes - startMinutes).coerceIn(0, total)
            } else {
                (24 * 60 - startMinutes + currentMinutes).coerceIn(0, total)
            }
        }
        return (elapsed.toFloat() / total.toFloat()).coerceIn(0f, 1f)
    }
}

data class Channel(
    val id: String,
    val number: Int,
    val name: String,
    val shortName: String = "",
    val country: Country,
    val category: TvCategory,
    val streamUrl: String,
    val backupStreamUrls: List<String> = emptyList(),
    val logoText: String,
    val brandColorHex: Long = 0xFF00E5FF,
    val description: String,
    val broadcastQuality: String = "HD 1080p",
    val schedule: List<ProgramItem>,
    val isFavorite: Boolean = false,
    val epgAliases: List<String> = emptyList(),
    val isRealEpg: Boolean = false
) {
    val displayShortName: String
        get() = if (shortName.isNotBlank()) shortName else {
            name.replace(" Costa Rica", "")
                .replace(" Televisión", "")
                .replace(" Señal Nacional", "")
                .replace(" Perú", "")
                .replace(" Peru", "")
                .trim()
        }

    fun getCurrentProgram(currentMinutes: Int, currentEpochMs: Long = System.currentTimeMillis()): ProgramItem? {
        val nowProgram = schedule.find { it.isCurrentlyAiring(currentMinutes, currentEpochMs) }
        if (nowProgram != null) return nowProgram

        // If real epoch timestamps are available, find the closest program around now
        if (schedule.isNotEmpty() && schedule.first().epochStartMs > 0L) {
            val upcoming = schedule.filter { it.epochEndMs > currentEpochMs }
            if (upcoming.isNotEmpty()) return upcoming.first()
        }

        return schedule.firstOrNull()
    }

    fun getNextProgram(currentMinutes: Int, currentEpochMs: Long = System.currentTimeMillis()): ProgramItem? {
        val currentIndex = schedule.indexOfFirst { it.isCurrentlyAiring(currentMinutes, currentEpochMs) }
        return if (currentIndex != -1 && currentIndex + 1 < schedule.size) {
            schedule[currentIndex + 1]
        } else if (schedule.isNotEmpty() && schedule.first().epochStartMs > 0L) {
            val futurePrograms = schedule.filter { it.epochStartMs > currentEpochMs }
            futurePrograms.firstOrNull() ?: if (schedule.size > 1) schedule[1] else null
        } else if (schedule.size > 1) {
            schedule[1]
        } else null
    }
}
