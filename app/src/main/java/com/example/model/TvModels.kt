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
    fun isCurrentlyAiring(currentMinutes: Int = -1, currentEpochMs: Long = System.currentTimeMillis()): Boolean {
        if (epochStartMs > 0L && epochEndMs > 0L) {
            return currentEpochMs in epochStartMs until epochEndMs
        }
        if (currentMinutes >= 0) {
            return if (endMinutes > startMinutes) {
                currentMinutes in startMinutes until endMinutes
            } else {
                // Over midnight
                currentMinutes >= startMinutes || currentMinutes < endMinutes
            }
        }
        return false
    }

    fun getProgressPercent(currentMinutes: Int = -1, currentEpochMs: Long = System.currentTimeMillis()): Float {
        if (epochStartMs > 0L && epochEndMs > 0L) {
            val duration = epochEndMs - epochStartMs
            if (duration <= 0L) return 0f
            return ((currentEpochMs - epochStartMs).toFloat() / duration.toFloat()).coerceIn(0f, 1f)
        }
        if (currentMinutes >= 0) {
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
        return 0f
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
    val schedule: List<ProgramItem> = emptyList(),
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

    fun getCurrentProgram(currentMinutes: Int = -1, currentEpochMs: Long = System.currentTimeMillis()): ProgramItem? {
        if (!isRealEpg || schedule.isEmpty()) return null

        // 1. Check exact epoch timestamps (Strict live broadcast time)
        val epochMatch = schedule.find {
            it.epochStartMs > 0L && it.epochEndMs > 0L && currentEpochMs in it.epochStartMs until it.epochEndMs
        }
        if (epochMatch != null) return epochMatch

        // 2. Check minute of day match if provided
        if (currentMinutes >= 0) {
            val minMatch = schedule.find { it.isCurrentlyAiring(currentMinutes, currentEpochMs) }
            if (minMatch != null) return minMatch
        }

        // Strictly return null if nothing is airing now. Do NOT invent or fallback to arbitrary programs!
        return null
    }

    fun getNextProgram(currentMinutes: Int = -1, currentEpochMs: Long = System.currentTimeMillis()): ProgramItem? {
        if (!isRealEpg || schedule.isEmpty()) return null

        val current = getCurrentProgram(currentMinutes, currentEpochMs)
        if (current != null) {
            val currentIndex = schedule.indexOfFirst { it.id == current.id }
            if (currentIndex != -1 && currentIndex + 1 < schedule.size) {
                return schedule[currentIndex + 1]
            }
            if (current.epochEndMs > 0L) {
                val nextByEpoch = schedule.filter { it.epochStartMs >= current.epochEndMs }.minByOrNull { it.epochStartMs }
                if (nextByEpoch != null) return nextByEpoch
            }
        } else {
            // Find first program scheduled after now
            val upcoming = schedule.filter { it.epochStartMs > currentEpochMs }.minByOrNull { it.epochStartMs }
            if (upcoming != null) return upcoming
        }

        // Strictly return null if no next program exists. Do NOT fallback!
        return null
    }
}
