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
import androidx.compose.material.icons.filled.SwapHoriz
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
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember(context) {
        context.getSharedPreferences("tv_mundial_player_prefs", Context.MODE_PRIVATE)
    }

    var showControls by remember { mutableStateOf(true) }
    var showFullscreenSwipeHint by remember { mutableStateOf(false) }

    // Ocultar controles automáticamente tras 3.5 segundos de inactividad
    LaunchedEffect(showControls, channel.id) {
        if (showControls) {
            delay(3500)
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
            .pointerInput(channel.id) {
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
                                // Prioridad de deslizamiento horizontal a los lados (izquierda = siguiente, derecha = anterior)
                                if (abs(totalDragX) >= abs(totalDragY)) {
                                    if (totalDragX < -40f) {
                                        onNextChannel()
                                        showControls = true
                                    } else if (totalDragX > 40f) {
                                        onPreviousChannel()
                                        showControls = true
                                    }
                                } else {
                                    // Deslizamiento vertical alternativo (arriba = siguiente, abajo = anterior)
                                    if (totalDragY < -40f) {
                                        onNextChannel()
                                        showControls = true
                                    } else if (totalDragY > 40f) {
                                        onPreviousChannel()
                                        showControls = true
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
                            if (abs(totalDragX) > 25f || abs(totalDragY) > 25f) {
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
                    player = playerManager.getPlayer()
                    resizeMode = playbackState.resizeMode
                }
            },
            update = { playerView ->
                if (playerView.player != playerManager.getPlayer()) {
                    playerView.player = playerManager.getPlayer()
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
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = null,
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Desliza a los lados para cambiar de canal",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
