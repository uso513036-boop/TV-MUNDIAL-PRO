package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Channel
import com.example.model.Country
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.Calendar

@Composable
fun ChannelListView(
    channels: List<Channel>,
    selectedChannel: Channel?,
    onSelectChannel: (Channel) -> Unit,
    onToggleFavorite: (Channel) -> Unit,
    selectedCountry: Country?,
    onSelectCountry: (Country?) -> Unit,
    onlyFavorites: Boolean = false,
    onToggleOnlyFavorites: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var currentEpochMs by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var currentMinutes by remember {
        val cal = Calendar.getInstance()
        mutableIntStateOf(cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE))
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            delay(30_000L) // Update every 30s
            currentEpochMs = System.currentTimeMillis()
            val cal = Calendar.getInstance()
            currentMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("channel_list_view")
    ) {
        // Country Filter Pills (Moved down here where categories were previously)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Todos los países
            CountryFilterPill(
                text = "Todos 🌐",
                isSelected = selectedCountry == null && !onlyFavorites,
                onClick = {
                    onToggleOnlyFavorites(false)
                    onSelectCountry(null)
                }
            )

            // Perú
            CountryFilterPill(
                text = "Perú 🇵🇪",
                isSelected = selectedCountry == Country.PERU && !onlyFavorites,
                onClick = {
                    onToggleOnlyFavorites(false)
                    onSelectCountry(Country.PERU)
                }
            )

            // Costa Rica
            CountryFilterPill(
                text = "Costa Rica 🇨🇷",
                isSelected = selectedCountry == Country.COSTA_RICA && !onlyFavorites,
                onClick = {
                    onToggleOnlyFavorites(false)
                    onSelectCountry(Country.COSTA_RICA)
                }
            )

            // Favoritos
            CountryFilterPill(
                text = "★ Favoritos",
                isSelected = onlyFavorites,
                onClick = {
                    onToggleOnlyFavorites(!onlyFavorites)
                }
            )
        }

        // Channels List
        if (channels.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No se encontraron canales",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Prueba seleccionando otra categoría o país.",
                        color = Color(0xFF64748B),
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(channels, key = { it.id }) { channel ->
                    val isCurrent = selectedChannel?.id == channel.id

                    // Get current live program strictly using real verified EPG
                    val currentProgram = channel.getCurrentProgram(currentMinutes, currentEpochMs)
                    val hasRealProgram = channel.isRealEpg && currentProgram != null
                    val onAirProgramTitle = if (hasRealProgram) currentProgram!!.title else "Programación no disponible"

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) Color(0xFF1E293B) else Color(0xFF0D1527)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectChannel(channel) }
                            .border(
                                width = if (isCurrent) 1.dp else 0.5.dp,
                                color = if (isCurrent) Color(0xFF00E5FF) else Color(0xFF1B283F),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .testTag("channel_item_${channel.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 1. Logo (Compact badge)
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = Color(channel.brandColorHex),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = channel.logoText,
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 10.5.sp,
                                        maxLines = 1
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // 2. Channel info: Short Name + Live On-Air Program
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                // Nombre del canal
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = channel.displayShortName,
                                        color = if (isCurrent) Color(0xFF00E5FF) else Color.White,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (hasRealProgram) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = Color(0xFF00C853).copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(3.dp)
                                        ) {
                                            Text(
                                                text = "EN VIVO",
                                                color = Color(0xFF69F0AE),
                                                fontSize = 7.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                // Programa que se está transmitiendo en ese momento (o "Programación no disponible")
                                Text(
                                    text = onAirProgramTitle,
                                    color = if (hasRealProgram) {
                                        if (isCurrent) Color(0xFF7DD3FC) else Color(0xFF94A3B8)
                                    } else {
                                        Color(0xFF64748B)
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = if (hasRealProgram) FontWeight.Medium else FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // 3. Category badge
                            Surface(
                                color = if (isCurrent) Color(0x2900E5FF) else Color(0xFF1A2333),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = channel.category.displayName,
                                    color = if (isCurrent) Color(0xFF00E5FF) else Color(0xFF8899AC),
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            // 4. Quick Favorite action
                            IconButton(
                                onClick = { onToggleFavorite(channel) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = if (channel.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                    contentDescription = "Favorito",
                                    tint = if (channel.isFavorite) Color(0xFFFFB300) else Color(0xFF4A5D78),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            // 5. Playing indicator
                            if (isCurrent) {
                                Spacer(modifier = Modifier.width(2.dp))
                                Surface(
                                    color = Color(0xFF00E5FF),
                                    shape = CircleShape,
                                    modifier = Modifier.size(6.dp)
                                ) {}
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
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
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color(0xFF00E5FF) else Color(0xFF0F172A),
        border = BorderStroke(
            1.dp,
            if (isSelected) Color(0xFF00E5FF) else Color(0xFF1E293B)
        ),
        modifier = modifier
            .height(34.dp)
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
        ) {
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

