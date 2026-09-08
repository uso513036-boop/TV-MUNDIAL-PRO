package com.example.ui.components

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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Channel
import com.example.model.Country
import com.example.model.TvCategory
import java.util.Calendar

@Composable
fun ChannelListView(
    channels: List<Channel>,
    selectedChannel: Channel?,
    onSelectChannel: (Channel) -> Unit,
    onToggleFavorite: (Channel) -> Unit,
    selectedCategory: TvCategory,
    onSelectCategory: (TvCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val calendar = remember { Calendar.getInstance() }
    val currentMinutes = remember {
        calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("channel_list_view")
    ) {
        // Category Filter Chips (Horizontal Scrollable)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TvCategory.values().forEach { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectCategory(category) },
                    label = {
                        Text(
                            text = category.displayName,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFF0F172A),
                        labelColor = Color(0xFF94A3B8),
                        selectedContainerColor = Color(0xFF00E5FF),
                        selectedLabelColor = Color(0xFF0F172A)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) Color(0xFF00E5FF) else Color(0xFF1E293B),
                        enabled = true,
                        selected = isSelected
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }
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
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(channels, key = { it.id }) { channel ->
                    val isCurrent = selectedChannel?.id == channel.id
                    val currentProg = channel.getCurrentProgram(currentMinutes)

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) Color(0xFF1E293B) else Color(0xFF0F172A)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectChannel(channel) }
                            .border(
                                width = if (isCurrent) 1.5.dp else 0.5.dp,
                                color = if (isCurrent) Color(0xFF00E5FF) else Color(0xFF1E293B),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .testTag("channel_item_${channel.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                // Channel Logo Box
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(channel.brandColorHex),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = channel.logoText,
                                            color = Color.White,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 14.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = channel.country.flag,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = channel.name,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        if (isCurrent) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = Color(0xFFFF1744),
                                                shape = RoundedCornerShape(3.dp)
                                            ) {
                                                Text(
                                                    text = "EN PANTALLA",
                                                    color = Color.White,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Black,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    // Current show
                                    Text(
                                        text = currentProg?.let { "▶ ${it.title}" } ?: "Emisión continua en vivo",
                                        color = if (isCurrent) Color(0xFF00E5FF) else Color(0xFFCBD5E1),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = "${channel.category.displayName} • ${channel.broadcastQuality} • ${channel.country.displayName}",
                                        color = Color(0xFF64748B),
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Favorite button
                                IconButton(
                                    onClick = { onToggleFavorite(channel) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = if (channel.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                        contentDescription = "Favorito",
                                        tint = if (channel.isFavorite) Color(0xFFFFB300) else Color(0xFF64748B),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                // Play Indicator
                                Surface(
                                    color = if (isCurrent) Color(0xFF00E5FF) else Color(0xFF1E293B),
                                    shape = CircleShape,
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Reproducir canal",
                                            tint = if (isCurrent) Color(0xFF0F172A) else Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
