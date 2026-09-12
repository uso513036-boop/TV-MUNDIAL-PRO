package com.example.ui.components

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.model.Channel
import com.example.player.TvPlayerManager
import com.example.player.VideoPlaybackState
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerView(
    channel: Channel,
    playerManager: TvPlayerManager,
    playbackState: VideoPlaybackState,
    isFullscreen: Boolean,
    onToggleFullscreen: () -> Unit,
    onNextChannel: () -> Unit,
    onPreviousChannel: () -> Unit,
    onRefreshEpg: (() -> Unit)? = null,
    isEpgSyncing: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember(context) {
        context.getSharedPreferences("tv_mundial_player_prefs", Context.MODE_PRIVATE)
    }

    var showControls by remember { mutableStateOf(true) }
    var showFullscreenSwipeHint by remember { mutableStateOf(false) }

    // Re-evaluar programa en emisión periódicamente
    var currentEpochMs by remember { mutableStateOf(System.currentTimeMillis()) }
    var currentTimeMinutes by remember {
        val cal = Calendar.getInstance()
        mutableStateOf(cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE))
    }

    LaunchedEffect(channel.id) {
        while (true) {
            val cal = Calendar.getInstance(TimeZone.getTimeZone(channel.country.timeZone))
            currentTimeMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
            currentEpochMs = System.currentTimeMillis()
            delay(15_000)
        }
    }

    val currentProgram = remember(channel, currentTimeMinutes, currentEpochMs) {
        channel.getCurrentProgram(currentTimeMinutes, currentEpochMs)
    }

    val nextProgram = remember(channel, currentTimeMinutes, currentEpochMs) {
        channel.getNextProgram(currentTimeMinutes, currentEpochMs)
    }

    // Ocultar controles automáticamente tras 4.5 segundos de inactividad
    LaunchedEffect(showControls, channel.id) {
        if (showControls) {
            delay(4500)
            showControls = false
        }
    }

    // Mostrar aviso una sola vez al entrar por primera vez a pantalla completa
    LaunchedEffect(isFullscreen) {
        if (isFullscreen) {
            val hasSeen = prefs.getBoolean("has_seen_fullscreen_swipe_hint", false)
            if (!hasSeen) {
                showFullscreenSwipeHint = true
                delay(3800)
                showFullscreenSwipeHint = false
                prefs.edit().putBoolean("has_seen_fullscreen_swipe_hint", true).apply()
            }
        } else {
            showFullscreenSwipeHint = false
        }
    }

    Box(
        modifier = modifier
            .background(Color.Black)
            .pointerInput(channel.id, isFullscreen) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    var totalDragX = 0f
                    var totalDragY = 0f
                    var hasDragged = false
                    val pointerId = down.id

                    while (true) {
                        val event = awaitPointerEvent()
                        val dragChange = event.changes.firstOrNull { it.id == pointerId }
                        if (dragChange == null || !dragChange.pressed) {
                            val wasConsumedByChild = dragChange?.isConsumed ?: false
                            if (hasDragged) {
                                if (isFullscreen) {
                                    // En pantalla completa: ÚNICAMENTE deslizar hacia arriba o hacia abajo para cambiar de canal
                                    if (abs(totalDragY) > abs(totalDragX) && abs(totalDragY) >= 35f) {
                                        if (totalDragY < 0f) {
                                            // Deslizar hacia arriba -> Siguiente canal
                                            onNextChannel()
                                            showControls = true
                                        } else {
                                            // Deslizar hacia abajo -> Canal anterior
                                            onPreviousChannel()
                                            showControls = true
                                        }
                                    }
                                } else {
                                    // En vista normal: tocar o deslizar
                                    if (abs(totalDragY) >= abs(totalDragX)) {
                                        if (totalDragY < -40f) {
                                            onNextChannel()
                                            showControls = true
                                        } else if (totalDragY > 40f) {
                                            onPreviousChannel()
                                            showControls = true
                                        }
                                    }
                                }
                            } else if (!wasConsumedByChild) {
                                // Tocar la pantalla -> Mostrar / ocultar controles
                                showControls = !showControls
                            }
                            break
                        } else {
                            val deltaX = dragChange.position.x - dragChange.previousPosition.x
                            val deltaY = dragChange.position.y - dragChange.previousPosition.y
                            totalDragX += deltaX
                            totalDragY += deltaY
                            if (abs(totalDragY) > 25f || (!isFullscreen && abs(totalDragX) > 25f)) {
                                hasDragged = true
                                dragChange.consume()
                            }
                        }
                    }
                }
            }
            .testTag("video_player_container")
    ) {
        // Embedded Android Media3 PlayerView
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    useController = false
                    setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)
                    setEnableComposeSurfaceSyncWorkaround(true)
                    player = playerManager.getPlayer()
                    resizeMode = playbackState.resizeMode
                }
            },
            update = { playerView ->
                val activePlayer = playerManager.getPlayer()
                if (playerView.player !== activePlayer) {
                    playerView.player = activePlayer
                }
                playerView.resizeMode = playbackState.resizeMode
            },
            onRelease = { playerView ->
                playerView.player = null
            },
            modifier = Modifier.fillMaxSize()
        )

        // Buffering Indicator
        if (playbackState.isBuffering && !playbackState.hasError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF00E5FF),
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(42.dp)
                )
            }
        }

        // Error Recovery Overlay
        if (playbackState.hasError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error de señal",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { playerManager.retryPlayback() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00E5FF),
                            contentColor = Color(0xFF0F172A)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("retry_stream_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reintentar señal", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Simplified Controls Overlay: Title + Main Play Button + EN VIVO
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.7f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            ) {
                // Top: Channel Title (left) & Direct Quick Action Buttons (right)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Only channel title
                    Text(
                        text = channel.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    // Direct controls: Aspect ratio, Mute, and Fullscreen toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Selector directo 16:9 (Original sin recortes) <-> 20:9 (Llenar pantalla)
                        Surface(
                            onClick = { playerManager.cycleResizeMode() },
                            color = if (playbackState.resizeMode == AspectRatioFrameLayout.RESIZE_MODE_FIT) {
                                Color.White.copy(alpha = 0.18f)
                            } else {
                                Color(0xFF00E5FF).copy(alpha = 0.25f)
                            },
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.testTag("player_aspect_ratio_chip")
                        ) {
                            Text(
                                text = if (playbackState.resizeMode == AspectRatioFrameLayout.RESIZE_MODE_FIT) "16:9" else "20:9",
                                color = if (playbackState.resizeMode == AspectRatioFrameLayout.RESIZE_MODE_FIT) Color.White else Color(0xFF00E5FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                            )
                        }

                        // Actualización de EPG disponible en pantalla completa
                        if (isFullscreen && onRefreshEpg != null) {
                            IconButton(
                                onClick = onRefreshEpg,
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("player_refresh_epg_button")
                            ) {
                                if (isEpgSyncing) {
                                    CircularProgressIndicator(
                                        color = Color(0xFF00E5FF),
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Sync,
                                        contentDescription = "Actualizar EPG",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = { playerManager.toggleMute() },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("player_mute_button")
                        ) {
                            Icon(
                                imageVector = if (playbackState.isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                                contentDescription = if (playbackState.isMuted) "Activar sonido" else "Silenciar",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        IconButton(
                            onClick = { onToggleFullscreen() },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("player_fullscreen_button")
                        ) {
                            Icon(
                                imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                                contentDescription = if (isFullscreen) "Salir de pantalla completa" else "Pantalla completa",
                                tint = Color(0xFF00E5FF),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Bottom Overlay: Guía EPG ("Estás viendo" + "Siguiente" + Horarios oficiales) - MOSTRAR EXCLUSIVAMENTE EN PANTALLA COMPLETA
                if (isFullscreen) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.4f)),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(
                                horizontal = 24.dp,
                                vertical = 16.dp
                            )
                            .testTag("player_epg_overlay")
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 10.dp
                            )
                        ) {
                            val hasRealCurrent = channel.isRealEpg && currentProgram != null
                            val hasRealNext = channel.isRealEpg && nextProgram != null

                            // Fila 1: 📺 "Estás viendo:" + Nombre del programa + ⏰ Horario de inicio y fin
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f, fill = false)
                                ) {
                                    Text(
                                        text = "📺 Estás viendo: ",
                                        color = Color(0xFF00E5FF),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp
                                    )
                                    Text(
                                        text = if (hasRealCurrent) currentProgram!!.title else "Programación no disponible",
                                        color = if (hasRealCurrent) Color.White else Color(0xFF94A3B8),
                                        fontWeight = if (hasRealCurrent) FontWeight.SemiBold else FontWeight.Normal,
                                        fontSize = 14.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                // ⏰ Horario de inicio y fin en formato de 12 horas (ej: "9:00 PM - 10:00 PM")
                                if (hasRealCurrent && (currentProgram!!.epochStartMs > 0L || currentProgram.startTime.isNotBlank())) {
                                    val start12 = formatTo12HourTime(currentProgram.epochStartMs, currentProgram.startTime, channel.country.timeZone)
                                    val end12 = formatTo12HourTime(currentProgram.epochEndMs, currentProgram.endTime, channel.country.timeZone)
                                    val timeRange12 = if (start12.isNotBlank() && end12.isNotBlank()) {
                                        "$start12 - $end12"
                                    } else if (start12.isNotBlank()) {
                                        start12
                                    } else ""

                                    if (timeRange12.isNotBlank()) {
                                        Surface(
                                            color = Color.White.copy(alpha = 0.14f),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = timeRange12,
                                                color = Color(0xFFFFD54F),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.5.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Barra de progreso del programa actual (Solo con datos reales)
                            if (hasRealCurrent) {
                                val progress = currentProgram!!.getProgressPercent(currentTimeMinutes, currentEpochMs)
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(3.5.dp)
                                        .clip(RoundedCornerShape(2.dp)),
                                    color = Color(0xFF00E5FF),
                                    trackColor = Color.White.copy(alpha = 0.18f),
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Fila 2: ⏭️ "Siguiente:" → Nombre del próximo programa + hora de inicio en formato de 12 horas
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "⏭️ Siguiente: ",
                                    color = Color(0xFFFFB74D),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.5.sp
                                )
                                Text(
                                    text = if (hasRealNext) {
                                        val nextStart12 = formatTo12HourTime(nextProgram!!.epochStartMs, nextProgram.startTime, channel.country.timeZone)
                                        if (nextStart12.isNotBlank()) {
                                            "${nextProgram.title} ($nextStart12)"
                                        } else {
                                            nextProgram.title
                                        }
                                    } else {
                                        "Programación no disponible"
                                    },
                                    color = if (hasRealNext) Color(0xFFE2E8F0) else Color(0xFF94A3B8),
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // Aviso una sola vez al entrar a pantalla completa (sin flechas que estorben)
        AnimatedVisibility(
            visible = showFullscreenSwipeHint,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.8f),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.5f)),
                modifier = Modifier.testTag("fullscreen_swipe_hint")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = null,
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Desliza arriba o abajo para cambiar de canal",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

private fun formatTo12HourTime(
    epochMs: Long,
    fallbackTimeStr: String,
    channelTimeZone: String
): String {
    if (epochMs > 0L) {
        val sdf = SimpleDateFormat("h:mm a", Locale.US).apply {
            timeZone = TimeZone.getDefault()
        }
        return sdf.format(Date(epochMs))
    }
    if (fallbackTimeStr.isBlank()) return ""
    return try {
        val sdf24 = SimpleDateFormat("HH:mm", Locale.US).apply {
            timeZone = TimeZone.getTimeZone(channelTimeZone)
        }
        val date = sdf24.parse(fallbackTimeStr)
        if (date != null) {
            val sdf12 = SimpleDateFormat("h:mm a", Locale.US).apply {
                timeZone = TimeZone.getDefault()
            }
            sdf12.format(date)
        } else {
            fallbackTimeStr
        }
    } catch (_: Exception) {
        fallbackTimeStr
    }
}

