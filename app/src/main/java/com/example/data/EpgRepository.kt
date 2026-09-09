package com.example.data

import android.content.Context
import android.util.Log
import com.example.model.Channel
import com.example.model.Country
import com.example.model.ProgramItem
import com.example.model.TvCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream

/**
 * EpgRepository manages fetching, streaming XMLTV parsing, and local daily caching
 * of real television programming guide data for Peru and Costa Rica.
 */
class EpgRepository(private val context: Context) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    private val cacheFileName = "real_epg_cache_v2.json"
    private val prefs = context.getSharedPreferences("epg_repo_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val TAG = "EpgRepository"
        private const val CACHE_MAX_AGE_MS = 24 * 60 * 60 * 1000L // 24 hours (1 day)
        private const val PREF_KEY_LAST_SYNC = "last_epg_sync_time"

        // Primary sources (fast gzip feeds updated daily)
        private const val URL_PE_PRIMARY = "https://epg.lat/files/pe.xml.gz"
        private const val URL_CR_PRIMARY = "https://epg.lat/files/cr.xml.gz"

        // Fallback sources (XMLTV raw XML / iptv-epg)
        private const val URL_PE_FALLBACK = "https://iptv-epg.org/files/epg-pe.xml"
        private const val URL_CR_FALLBACK = "https://iptv-epg.org/files/epg-cr.xml"
    }

    /**
     * Checks if cached EPG data is still fresh (less than 24h old).
     */
    fun isCacheValid(): Boolean {
        val lastSync = prefs.getLong(PREF_KEY_LAST_SYNC, 0L)
        val file = File(context.filesDir, cacheFileName)
        return file.exists() && (System.currentTimeMillis() - lastSync) < CACHE_MAX_AGE_MS
    }

    fun getLastSyncTimestamp(): Long {
        return prefs.getLong(PREF_KEY_LAST_SYNC, 0L)
    }

    /**
     * Loads programs from local cache immediately into channels.
     */
    fun applyCachedEpg(channels: List<Channel>): List<Channel> {
        val cacheFile = File(context.filesDir, cacheFileName)
        if (!cacheFile.exists()) return channels

        return try {
            val jsonString = cacheFile.readText()
            val root = JSONObject(jsonString)
            val channelsObj = root.optJSONObject("channels") ?: return channels

            channels.map { channel ->
                val channelProgramsArray = channelsObj.optJSONArray(channel.id)
                if (channelProgramsArray != null && channelProgramsArray.length() > 0) {
                    val realPrograms = mutableListOf<ProgramItem>()
                    for (i in 0 until channelProgramsArray.length()) {
                        val pObj = channelProgramsArray.getJSONObject(i)
                        realPrograms.add(
                            ProgramItem(
                                id = pObj.getString("id"),
                                title = pObj.getString("title"),
                                description = pObj.optString("description", ""),
                                category = try {
                                    TvCategory.valueOf(pObj.optString("category", TvCategory.ENTRETENIMIENTO.name))
                                } catch (_: Exception) {
                                    TvCategory.ENTRETENIMIENTO
                                },
                                startTime = pObj.getString("startTime"),
                                endTime = pObj.getString("endTime"),
                                startMinutes = pObj.getInt("startMinutes"),
                                endMinutes = pObj.getInt("endMinutes"),
                                rating = pObj.optString("rating", "TP"),
                                hostOrStar = pObj.optString("hostOrStar", ""),
                                epochStartMs = pObj.optLong("epochStartMs", 0L),
                                epochEndMs = pObj.optLong("epochEndMs", 0L)
                            )
                        )
                    }
                    channel.copy(schedule = realPrograms, isRealEpg = true)
                } else {
                    channel
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error applying cached EPG", e)
            channels
        }
    }

    /**
     * Fetches official XMLTV guide feeds, parses real schedules, updates local cache,
     * and returns the updated channels list.
     */
    suspend fun syncEpg(channels: List<Channel>, force: Boolean = false): List<Channel> = withContext(Dispatchers.IO) {
        if (!force && isCacheValid()) {
            Log.d(TAG, "EPG Cache is still valid today. Using cached guide.")
            return@withContext applyCachedEpg(channels)
        }

        Log.d(TAG, "Starting download and parsing of real XMLTV EPG data...")
        val programsByChannelId = mutableMapOf<String, MutableList<ProgramItem>>()

        // Build matching index for channels
        val channelMatchMap = buildChannelMatchMap(channels)

        // 1. Download & Parse Peru EPG
        try {
            fetchAndParseXmltv(URL_PE_PRIMARY, URL_PE_FALLBACK, Country.PERU, channelMatchMap, programsByChannelId)
        } catch (e: Exception) {
            Log.w(TAG, "Failed parsing Peru XMLTV from primary/fallback: ${e.message}")
        }

        // 2. Download & Parse Costa Rica EPG
        try {
            fetchAndParseXmltv(URL_CR_PRIMARY, URL_CR_FALLBACK, Country.COSTA_RICA, channelMatchMap, programsByChannelId)
        } catch (e: Exception) {
            Log.w(TAG, "Failed parsing Costa Rica XMLTV from primary/fallback: ${e.message}")
        }

        // 3. Save to local disk cache for fast daily reuse
        saveToCache(programsByChannelId)
        prefs.edit().putLong(PREF_KEY_LAST_SYNC, System.currentTimeMillis()).apply()

        // 4. Merge into channel list
        val updated = channels.map { channel ->
            val fetchedPrograms = programsByChannelId[channel.id]
            if (!fetchedPrograms.isNullOrEmpty()) {
                // Sort programs by time
                val sorted = fetchedPrograms.sortedBy { if (it.epochStartMs > 0L) it.epochStartMs else it.startMinutes.toLong() }
                channel.copy(schedule = sorted, isRealEpg = true)
            } else {
                channel
            }
        }

        updated
    }

    private fun buildChannelMatchMap(channels: List<Channel>): Map<String, Channel> {
        val map = mutableMapOf<String, Channel>()
        channels.forEach { ch ->
            // Match by ID
            map[ch.id.lowercase()] = ch
            // Match by normalized name
            map[normalizeString(ch.name)] = ch
            if (ch.shortName.isNotBlank()) {
                map[normalizeString(ch.shortName)] = ch
            }
            // Match by aliases
            ch.epgAliases.forEach { alias ->
                map[normalizeString(alias)] = ch
                map[alias.lowercase()] = ch
            }
        }
        return map
    }

    private fun normalizeString(input: String): String {
        return input.lowercase(Locale.ROOT)
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
            .replace("ñ", "n")
            .replace(Regex("[^a-z0-9]"), "")
    }

    private fun fetchAndParseXmltv(
        primaryUrl: String,
        fallbackUrl: String,
        country: Country,
        channelMatchMap: Map<String, Channel>,
        outPrograms: MutableMap<String, MutableList<ProgramItem>>
    ) {
        var success = false
        try {
            downloadAndParseStream(primaryUrl, isGzip = primaryUrl.endsWith(".gz"), country, channelMatchMap, outPrograms)
            success = true
        } catch (e: Exception) {
            Log.w(TAG, "Error downloading primary URL $primaryUrl: ${e.message}. Trying fallback...")
        }

        if (!success) {
            try {
                downloadAndParseStream(fallbackUrl, isGzip = fallbackUrl.endsWith(".gz"), country, channelMatchMap, outPrograms)
            } catch (e: Exception) {
                Log.e(TAG, "Error downloading fallback URL $fallbackUrl: ${e.message}")
            }
        }
    }

    private fun downloadAndParseStream(
        url: String,
        isGzip: Boolean,
        country: Country,
        channelMatchMap: Map<String, Channel>,
        outPrograms: MutableMap<String, MutableList<ProgramItem>>
    ) {
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0 (Android; Mobile; TV-Mundial-App)")
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) {
            response.close()
            throw IllegalStateException("HTTP ${response.code} for $url")
        }

        val bodyStream = response.body?.byteStream() ?: throw IllegalStateException("Empty response body")
        val inputStream: InputStream = if (isGzip) GZIPInputStream(bodyStream) else bodyStream

        inputStream.use { stream ->
            parseXmltvPull(stream, country, channelMatchMap, outPrograms)
        }
    }

    private fun parseXmltvPull(
        inputStream: InputStream,
        country: Country,
        channelMatchMap: Map<String, Channel>,
        outPrograms: MutableMap<String, MutableList<ProgramItem>>
    ) {
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = false
        val parser = factory.newPullParser()
        parser.setInput(inputStream, "UTF-8")

        // Map XMLTV channel id to matched app Channel
        val xmltvIdToAppChannel = mutableMapOf<String, Channel>()

        val targetTimeZone = TimeZone.getTimeZone(country.timeZone)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.US).apply {
            timeZone = targetTimeZone
        }
        val cal = Calendar.getInstance(targetTimeZone)

        var eventType = parser.eventType
        var currentXmlChannelId: String? = null
        var currentXmlDisplayName: String? = null

        var currentProgChannelId: String? = null
        var currentProgStart: String? = null
        var currentProgStop: String? = null
        var currentProgTitle: String? = null
        var currentProgDesc: String? = null
        var currentProgCategory: String? = null
        var currentProgRating: String? = null

        var currentTagName: String? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    currentTagName = parser.name
                    when (currentTagName) {
                        "channel" -> {
                            currentXmlChannelId = parser.getAttributeValue(null, "id")
                            currentXmlDisplayName = null
                        }
                        "programme" -> {
                            currentProgChannelId = parser.getAttributeValue(null, "channel")
                            currentProgStart = parser.getAttributeValue(null, "start")
                            currentProgStop = parser.getAttributeValue(null, "stop")
                            currentProgTitle = null
                            currentProgDesc = null
                            currentProgCategory = null
                            currentProgRating = null
                        }
                    }
                }
                XmlPullParser.TEXT -> {
                    val text = parser.text?.trim()
                    if (!text.isNullOrEmpty()) {
                        when (currentTagName) {
                            "display-name" -> {
                                if (currentXmlDisplayName == null) {
                                    currentXmlDisplayName = text
                                }
                            }
                            "title" -> {
                                if (currentProgTitle == null) currentProgTitle = text
                            }
                            "desc" -> {
                                if (currentProgDesc == null) currentProgDesc = text
                            }
                            "category" -> {
                                if (currentProgCategory == null) currentProgCategory = text
                            }
                            "value" -> {
                                if (currentProgRating == null) currentProgRating = text
                            }
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    val endName = parser.name
                    when (endName) {
                        "channel" -> {
                            // Find matching app channel for this XMLTV channel
                            val xmlId = currentXmlChannelId ?: ""
                            val disp = currentXmlDisplayName ?: ""
                            val matched = findMatchingChannel(xmlId, disp, channelMatchMap)
                            if (matched != null) {
                                xmltvIdToAppChannel[xmlId] = matched
                                Log.d(TAG, "Matched XMLTV channel '$xmlId' ($disp) -> App Channel: ${matched.name}")
                            }
                            currentXmlChannelId = null
                            currentXmlDisplayName = null
                        }
                        "programme" -> {
                            val xmlId = currentProgChannelId ?: ""
                            val targetChannel = xmltvIdToAppChannel[xmlId] ?: findMatchingChannel(xmlId, "", channelMatchMap)

                            if (targetChannel != null && !currentProgTitle.isNullOrBlank() && !currentProgStart.isNullOrBlank()) {
                                val parsedStart = parseXmltvTimestamp(currentProgStart!!)
                                val parsedEnd = if (!currentProgStop.isNullOrBlank()) parseXmltvTimestamp(currentProgStop!!) else null

                                if (parsedStart != null) {
                                    val startEpoch = parsedStart.time
                                    val endEpoch = parsedEnd?.time ?: (startEpoch + 60 * 60 * 1000L)

                                    cal.time = parsedStart
                                    val startMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
                                    val startTimeStr = timeFormat.format(parsedStart)

                                    cal.time = Date(endEpoch)
                                    val endMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
                                    val endTimeStr = timeFormat.format(Date(endEpoch))

                                    val mappedCategory = mapCategory(currentProgCategory, currentProgTitle!!)

                                    val program = ProgramItem(
                                        id = "${targetChannel.id}_${startEpoch}",
                                        title = currentProgTitle!!.trim(),
                                        description = currentProgDesc?.trim() ?: "Programación oficial transmitida por ${targetChannel.name}.",
                                        category = mappedCategory,
                                        startTime = startTimeStr,
                                        endTime = endTimeStr,
                                        startMinutes = startMinutes,
                                        endMinutes = endMinutes,
                                        rating = currentProgRating?.trim() ?: "TP",
                                        epochStartMs = startEpoch,
                                        epochEndMs = endEpoch
                                    )

                                    val list = outPrograms.getOrPut(targetChannel.id) { mutableListOf() }
                                    list.add(program)
                                }
                            }

                            currentProgChannelId = null
                            currentProgStart = null
                            currentProgStop = null
                            currentProgTitle = null
                            currentProgDesc = null
                            currentProgCategory = null
                            currentProgRating = null
                        }
                    }
                    currentTagName = null
                }
            }
            eventType = parser.next()
        }
    }

    private fun findMatchingChannel(xmlId: String, displayName: String, channelMatchMap: Map<String, Channel>): Channel? {
        val normId = normalizeString(xmlId)
        val normDisp = normalizeString(displayName)

        // Exact match on id or normalized id
        channelMatchMap[xmlId.lowercase()]?.let { return it }
        channelMatchMap[normId]?.let { return it }

        // Exact match on display name
        if (normDisp.isNotEmpty()) {
            channelMatchMap[normDisp]?.let { return it }
        }

        // Substring / fuzzy match
        for ((key, channel) in channelMatchMap) {
            if (key.length >= 4) {
                if (normId.contains(key) || (normDisp.isNotEmpty() && normDisp.contains(key))) {
                    return channel
                }
            }
        }
        return null
    }

    private fun parseXmltvTimestamp(raw: String): Date? {
        val trimmed = raw.trim()
        val patterns = listOf(
            "yyyyMMddHHmmss Z",
            "yyyyMMddHHmmss",
            "yyyyMMddHHmm Z",
            "yyyyMMddHHmm"
        )
        for (pattern in patterns) {
            try {
                val format = SimpleDateFormat(pattern, Locale.US)
                if (!pattern.contains("Z")) {
                    format.timeZone = TimeZone.getTimeZone("UTC")
                }
                return format.parse(trimmed)
            } catch (_: Exception) { }
        }
        return null
    }

    private fun mapCategory(xmlCategory: String?, title: String): TvCategory {
        val cat = (xmlCategory ?: "").lowercase(Locale.ROOT)
        val t = title.lowercase(Locale.ROOT)

        return when {
            cat.contains("notic") || cat.contains("news") || t.contains("noticias") || t.contains("telediario") || t.contains("edicion") -> TvCategory.NOTICIAS
            cat.contains("deport") || cat.contains("sport") || cat.contains("futbol") || t.contains("futbol") || t.contains("deport") -> TvCategory.DEPORTES
            cat.contains("infantil") || cat.contains("kid") || cat.contains("animac") || cat.contains("dibujo") -> TvCategory.INFANTIL
            cat.contains("music") || t.contains("musica") || t.contains("rock") || t.contains("acustico") -> TvCategory.MUSICA
            cat.contains("cultur") || cat.contains("educa") || cat.contains("docu") || t.contains("cultura") || t.contains("historia") -> TvCategory.CULTURA
            else -> TvCategory.ENTRETENIMIENTO
        }
    }

    private fun saveToCache(programsByChannelId: Map<String, List<ProgramItem>>) {
        try {
            val root = JSONObject()
            root.put("timestamp", System.currentTimeMillis())
            val channelsObj = JSONObject()

            for ((chId, programs) in programsByChannelId) {
                val arr = JSONArray()
                for (p in programs) {
                    val pObj = JSONObject().apply {
                        put("id", p.id)
                        put("title", p.title)
                        put("description", p.description)
                        put("category", p.category.name)
                        put("startTime", p.startTime)
                        put("endTime", p.endTime)
                        put("startMinutes", p.startMinutes)
                        put("endMinutes", p.endMinutes)
                        put("rating", p.rating)
                        put("hostOrStar", p.hostOrStar)
                        put("epochStartMs", p.epochStartMs)
                        put("epochEndMs", p.epochEndMs)
                    }
                    arr.put(pObj)
                }
                channelsObj.put(chId, arr)
            }
            root.put("channels", channelsObj)

            val cacheFile = File(context.filesDir, cacheFileName)
            cacheFile.writeText(root.toString())
            Log.d(TAG, "Cached real EPG data for ${programsByChannelId.size} channels successfully to ${cacheFile.name}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed saving EPG cache to disk", e)
        }
    }
}
