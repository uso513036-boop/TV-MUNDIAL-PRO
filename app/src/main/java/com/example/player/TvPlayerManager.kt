package com.example.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.model.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class VideoPlaybackState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String? = null,
    val isMuted: Boolean = false,
    val volume: Float = 1.0f,
    val activeStreamUrl: String = "",
    val isUsingBackup: Boolean = false,
    val resizeMode: Int = 0 // 0 = FIT (ajuste automático: muestra 100% de la imagen sin recortar ni deformar)
)

@OptIn(UnstableApi::class)
class TvPlayerManager(private val context: Context) {

    private var exoPlayer: ExoPlayer? = null
    private var currentChannel: Channel? = null
    private var currentStreamIndex = 0

    private val _playbackState = MutableStateFlow(VideoPlaybackState())
    val playbackState: StateFlow<VideoPlaybackState> = _playbackState.asStateFlow()

    fun getPlayer(): ExoPlayer {
        if (exoPlayer == null) {
            val loadControl = DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                    15000, // minBufferMs: safe buffer for smooth live stream playback
                    30000, // maxBufferMs
                    1500,  // bufferForPlaybackMs: starts playing after 1.5s
                    2500   // bufferForPlaybackAfterRebufferMs: resumes after 2.5s
                )
                .build()

            val httpDataSourceFactory = DefaultHttpDataSource.Factory()
                .setUserAgent("Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36")
                .setConnectTimeoutMs(10000)
                .setReadTimeoutMs(15000)
                .setAllowCrossProtocolRedirects(true)

            val mediaSourceFactory = DefaultMediaSourceFactory(httpDataSourceFactory)

            exoPlayer = ExoPlayer.Builder(context)
                .setMediaSourceFactory(mediaSourceFactory)
                .setLoadControl(loadControl)
                .build()
                .apply {
                    repeatMode = Player.REPEAT_MODE_OFF
                    playWhenReady = true
                    addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            val buffering = playbackState == Player.STATE_BUFFERING
                            val playing = isPlaying && playbackState == Player.STATE_READY
                            _playbackState.value = _playbackState.value.copy(
                                isBuffering = buffering,
                                isPlaying = playing,
                                hasError = false,
                                errorMessage = null
                            )
                        }

                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            _playbackState.value = _playbackState.value.copy(isPlaying = isPlaying)
                        }

                        override fun onPlayerError(error: PlaybackException) {
                            handlePlaybackError(error)
                        }
                    })
                }
        }
        return exoPlayer!!
    }

    fun playChannel(channel: Channel) {
        currentChannel = channel
        currentStreamIndex = 0
        loadStream(channel.streamUrl, isBackup = false)
    }

    private fun loadStream(url: String, isBackup: Boolean) {
        val player = getPlayer()
        _playbackState.value = _playbackState.value.copy(
            isBuffering = true,
            hasError = false,
            errorMessage = null,
            activeStreamUrl = url,
            isUsingBackup = isBackup
        )

        try {
            val mediaItemBuilder = MediaItem.Builder().setUri(url)
            if (url.contains(".m3u8", ignoreCase = true)) {
                mediaItemBuilder.setMimeType(MimeTypes.APPLICATION_M3U8)
            }
            val mediaItem = mediaItemBuilder.build()
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
        } catch (e: Exception) {
            handlePlaybackError(null)
        }
    }

    private fun handlePlaybackError(error: PlaybackException?) {
        val channel = currentChannel ?: return
        val backups = channel.backupStreamUrls

        if (currentStreamIndex < backups.size) {
            val nextUrl = backups[currentStreamIndex]
            currentStreamIndex++
            _playbackState.value = _playbackState.value.copy(
                isBuffering = true,
                errorMessage = "Cambiando a servidor de respaldo...",
                hasError = false
            )
            loadStream(nextUrl, isBackup = true)
        } else {
            _playbackState.value = _playbackState.value.copy(
                isBuffering = false,
                isPlaying = false,
                hasError = true,
                errorMessage = "Señal en reconexión. Toca 'Reintentar' para recargar la señal."
            )
        }
    }

    fun retryPlayback() {
        val channel = currentChannel ?: return
        currentStreamIndex = 0
        loadStream(channel.streamUrl, isBackup = false)
    }

    fun togglePlayPause() {
        val player = exoPlayer ?: return
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun toggleMute() {
        val player = exoPlayer ?: return
        val currentMute = _playbackState.value.isMuted
        if (currentMute) {
            player.volume = 1.0f
            _playbackState.value = _playbackState.value.copy(isMuted = false, volume = 1.0f)
        } else {
            player.volume = 0.0f
            _playbackState.value = _playbackState.value.copy(isMuted = true, volume = 0.0f)
        }
    }

    fun setVolume(volume: Float) {
        val player = exoPlayer ?: return
        val clamped = volume.coerceIn(0f, 1f)
        player.volume = clamped
        _playbackState.value = _playbackState.value.copy(volume = clamped, isMuted = clamped == 0f)
    }

    fun setResizeMode(mode: Int) {
        _playbackState.value = _playbackState.value.copy(resizeMode = mode)
    }

    fun cycleResizeMode() {
        // 4: ZOOM (16:9 / aprovecha pantalla sin bordes), 3: FILL (estirar), 0: FIT (original)
        val nextMode = when (_playbackState.value.resizeMode) {
            4 -> 3
            3 -> 0
            else -> 4
        }
        _playbackState.value = _playbackState.value.copy(resizeMode = nextMode)
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun resume() {
        exoPlayer?.play()
    }

    fun release() {
        exoPlayer?.release()
        exoPlayer = null
    }
}
