package com.example.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Country
import com.example.player.TvPlayerManager
import com.example.ui.components.ChannelListView
import com.example.ui.components.EpgScheduleView
import com.example.ui.components.VideoPlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvMundialApp(
    viewModel: TvMundialViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? Activity

    val playerManager = remember { TvPlayerManager(context) }
    val playbackState by playerManager.playbackState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var isSearchExpanded by remember { mutableStateOf(false) }

    // Start playback whenever selectedChannel changes
    LaunchedEffect(uiState.selectedChannel?.id) {
        uiState.selectedChannel?.let { channel ->
            playerManager.playChannel(channel)
        }
    }

    // Show snackbar notifications
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbar()
        }
    }

    // Handle screen orientation and immersive fullscreen (hiding system status & navigation bars)
    LaunchedEffect(uiState.isFullscreen) {
        val window = activity?.window
        if (window != null) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            if (uiState.isFullscreen) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                insetsController.hide(WindowInsetsCompat.Type.systemBars())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    window.attributes = window.attributes.apply {
                        layoutInDisplayCutoutMode =
                            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                    }
                }
            } else {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                insetsController.show(WindowInsetsCompat.Type.systemBars())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    window.attributes = window.attributes.apply {
                        layoutInDisplayCutoutMode =
                            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
                    }
                }
            }
        }
    }

    // Clean up player and restore system bars on leave
    DisposableEffect(Unit) {
        onDispose {
            playerManager.release()
            activity?.let { act ->
                act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                val insetsController = WindowCompat.getInsetsController(act.window, act.window.decorView)
                insetsController.show(WindowInsetsCompat.Type.systemBars())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    act.window.attributes = act.window.attributes.apply {
                        layoutInDisplayCutoutMode =
                            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
                    }
                }
            }
        }
    }

    // Handle back button on fullscreen
    BackHandler(enabled = uiState.isFullscreen) {
        viewModel.setFullscreen(false)
    }

    // FULLSCREEN PLAYER MODE
    if (uiState.isFullscreen && uiState.selectedChannel != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            VideoPlayerView(
                channel = uiState.selectedChannel!!,
                playerManager = playerManager,
                playbackState = playbackState,
                isFullscreen = true,
                onToggleFullscreen = { viewModel.toggleFullscreen() },
                onNextChannel = { viewModel.selectNextChannel() },
                onPreviousChannel = { viewModel.selectPreviousChannel() },
                onRefreshEpg = { viewModel.syncEpg(force = true) },
                isEpgSyncing = uiState.isEpgSyncing,
                modifier = Modifier.fillMaxSize()
            )
        }
        return
    }

    // NORMAL APP LAYOUT
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070B14)),
        containerColor = Color(0xFF070B14),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0B111E))
                    .statusBarsPadding()
            ) {
                // Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // App Logo & Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.testTag("app_brand_header")
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF00E5FF),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.LiveTv,
                                    contentDescription = null,
                                    tint = Color(0xFF070B14),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "TV MUNDIAL",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    color = Color(0xFFFFB300),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "PRO",
                                        color = Color(0xFF0F172A),
                                        fontWeight = FontWeight.Black,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Perú 🇵🇪 & Costa Rica 🇨🇷 en vivo",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Search Toggle
                    IconButton(
                        onClick = { isSearchExpanded = !isSearchExpanded },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("search_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Buscar canales",
                            tint = Color(0xFF00E5FF)
                        )
                    }
                }

                // Search Input Field
                AnimatedVisibility(visible = isSearchExpanded) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("Buscar canal o programa...", color = Color(0xFF64748B), fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF00E5FF))
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = Color(0xFF94A3B8))
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00E5FF),
                            unfocusedBorderColor = Color(0xFF1E293B),
                            focusedContainerColor = Color(0xFF0F172A),
                            unfocusedContainerColor = Color(0xFF0F172A),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .testTag("search_text_field")
                    )
                }

                // Country Selector Pills (Peru, Costa Rica, Todos, Favoritos)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Perú Pill
                    CountryFilterPill(
                        text = "Perú 🇵🇪",
                        isSelected = uiState.selectedCountry == Country.PERU && !uiState.onlyFavorites,
                        onClick = {
                            viewModel.setOnlyFavorites(false)
                            viewModel.setCountryFilter(Country.PERU)
                        },
                        modifier = Modifier.weight(1f)
                    )

                    // Costa Rica Pill
                    CountryFilterPill(
                        text = "Costa Rica 🇨🇷",
                        isSelected = uiState.selectedCountry == Country.COSTA_RICA && !uiState.onlyFavorites,
                        onClick = {
                            viewModel.setOnlyFavorites(false)
                            viewModel.setCountryFilter(Country.COSTA_RICA)
                        },
                        modifier = Modifier.weight(1.2f)
                    )

                    // Todos Pill
                    CountryFilterPill(
                        text = "Todos 🌐",
                        isSelected = uiState.selectedCountry == null && !uiState.onlyFavorites,
                        onClick = {
                            viewModel.setOnlyFavorites(false)
                            viewModel.setCountryFilter(null)
                        },
                        modifier = Modifier.weight(0.9f)
                    )

                    // Favoritos Pill
                    CountryFilterPill(
                        text = "★",
                        isSelected = uiState.onlyFavorites,
                        onClick = {
                            viewModel.setOnlyFavorites(!uiState.onlyFavorites)
                        },
                        modifier = Modifier.width(42.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF0B111E),
                contentColor = Color.White,
                modifier = Modifier.navigationBarsPadding()
            ) {
                AppTab.values().forEach { tab ->
                    val isSelected = uiState.activeTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setActiveTab(tab) },
                        icon = {
                            Icon(
                                imageVector = when (tab) {
                                    AppTab.EN_VIVO -> Icons.Default.LiveTv
                                    AppTab.GUIA_EPG -> Icons.Default.Tv
                                    AppTab.CANALES -> Icons.Default.Public
                                },
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF0F172A),
                            selectedTextColor = Color(0xFF00E5FF),
                            indicatorColor = Color(0xFF00E5FF),
                            unselectedIconColor = Color(0xFF64748B),
                            unselectedTextColor = Color(0xFF64748B)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // EMBEDDED VIDEO PLAYER (Pinned at top)
            if (uiState.selectedChannel != null) {
                VideoPlayerView(
                    channel = uiState.selectedChannel!!,
                    playerManager = playerManager,
                    playbackState = playbackState,
                    isFullscreen = false,
                    onToggleFullscreen = { viewModel.toggleFullscreen() },
                    onNextChannel = { viewModel.selectNextChannel() },
                    onPreviousChannel = { viewModel.selectPreviousChannel() },
                    onRefreshEpg = { viewModel.syncEpg(force = true) },
                    isEpgSyncing = uiState.isEpgSyncing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color(0xFF0F172A)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Selecciona un canal para comenzar a ver", color = Color(0xFF94A3B8), fontSize = 13.sp)
                }
            }

            // CONTENT BASED ON ACTIVE TAB
            when (uiState.activeTab) {
                AppTab.EN_VIVO -> {
                    // Quick Zap Channel List + Live Info
                    ChannelListView(
                        channels = uiState.filteredChannels,
                        selectedChannel = uiState.selectedChannel,
                        onSelectChannel = {
                            viewModel.selectChannel(it)
                            playerManager.playChannel(it)
                        },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        selectedCategory = uiState.selectedCategory,
                        onSelectCategory = { viewModel.setCategoryFilter(it) },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                AppTab.GUIA_EPG -> {
                    // Customized EPG Programming Guide for Selected Channel
                    if (uiState.selectedChannel != null) {
                        EpgScheduleView(
                            channel = uiState.selectedChannel!!,
                            onPlayChannel = { playerManager.playChannel(uiState.selectedChannel!!) },
                            onToggleFavorite = { viewModel.toggleFavorite(uiState.selectedChannel!!) },
                            onToggleReminder = { program ->
                                viewModel.toggleReminder(program, uiState.selectedChannel!!.name)
                            },
                            remindersState = uiState.reminderIds,
                            onRefreshEpg = { viewModel.syncEpg(force = true) },
                            isEpgSyncing = uiState.isEpgSyncing,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Selecciona un canal para ver su guía EPG", color = Color(0xFF64748B))
                        }
                    }
                }

                AppTab.CANALES -> {
                    // Full Channel Explorer & Categories
                    ChannelListView(
                        channels = uiState.filteredChannels,
                        selectedChannel = uiState.selectedChannel,
                        onSelectChannel = {
                            viewModel.selectChannel(it)
                            playerManager.playChannel(it)
                            viewModel.setActiveTab(AppTab.EN_VIVO)
                        },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        selectedCategory = uiState.selectedCategory,
                        onSelectCategory = { viewModel.setCategoryFilter(it) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun CountryFilterPill(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF00E5FF) else Color(0xFF131C2E),
        modifier = modifier
            .height(34.dp)
            .clickable { onClick() }
            .border(
                1.dp,
                if (isSelected) Color(0xFF00E5FF) else Color(0xFF1E293B),
                RoundedCornerShape(20.dp)
            )
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (isSelected) Color(0xFF070B14) else Color(0xFFCBD5E1),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
    }
}
