package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ChannelRepository
import com.example.data.EpgRepository
import com.example.model.Channel
import com.example.model.Country
import com.example.model.ProgramItem
import com.example.model.TvCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppTab(val title: String) {
    EN_VIVO("En Vivo"),
    GUIA_EPG("Guía EPG"),
    CANALES("Canales")
}

data class TvUiState(
    val allChannels: List<Channel> = emptyList(),
    val selectedChannel: Channel? = null,
    val selectedCountry: Country? = null, // Default to null (Todos los países) so all available channels are shown
    val selectedCategory: TvCategory = TvCategory.TODOS,
    val onlyFavorites: Boolean = false,
    val searchQuery: String = "",
    val activeTab: AppTab = AppTab.EN_VIVO,
    val isFullscreen: Boolean = false,
    val favoriteIds: Set<String> = emptySet(),
    val reminderIds: Set<String> = emptySet(),
    val snackbarMessage: String? = null,
    val isEpgSyncing: Boolean = false,
    val lastEpgSyncTime: Long = 0L
) {
    val filteredChannels: List<Channel>
        get() {
            return allChannels.filter { channel ->
                // Country filter
                val matchesCountry = selectedCountry == null || channel.country == selectedCountry
                // Category filter
                val matchesCategory = selectedCategory == TvCategory.TODOS || channel.category == selectedCategory
                // Favorite filter
                val matchesFavorite = !onlyFavorites || favoriteIds.contains(channel.id)
                // Search query
                val matchesSearch = if (searchQuery.isBlank()) true else {
                    channel.name.contains(searchQuery, ignoreCase = true) ||
                    channel.category.displayName.contains(searchQuery, ignoreCase = true) ||
                    channel.schedule.any { it.title.contains(searchQuery, ignoreCase = true) }
                }

                matchesCountry && matchesCategory && matchesFavorite && matchesSearch
            }.map { channel ->
                channel.copy(isFavorite = favoriteIds.contains(channel.id))
            }
        }
}

class TvMundialViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("tv_mundial_prefs", Context.MODE_PRIVATE)
    private val epgRepository = EpgRepository(application)

    private val _uiState = MutableStateFlow(TvUiState())
    val uiState: StateFlow<TvUiState> = _uiState.asStateFlow()

    init {
        loadChannels()
        syncEpg(force = false)
    }

    private fun loadChannels() {
        val savedFavorites = prefs.getStringSet("favorite_ids", emptySet()) ?: emptySet()
        val savedReminders = prefs.getStringSet("reminder_ids", emptySet()) ?: emptySet()

        val rawChannels = ChannelRepository.getChannels()
        val channelsWithFavs = rawChannels.map {
            it.copy(isFavorite = savedFavorites.contains(it.id))
        }

        // Apply fast cached real EPG if available from previous runs
        val channelsWithCachedEpg = epgRepository.applyCachedEpg(channelsWithFavs)
        val initialChannel = channelsWithCachedEpg.firstOrNull()

        _uiState.value = TvUiState(
            allChannels = channelsWithCachedEpg,
            selectedChannel = initialChannel,
            selectedCountry = null, // Default to null (Todos los países)
            favoriteIds = savedFavorites,
            reminderIds = savedReminders,
            lastEpgSyncTime = epgRepository.getLastSyncTimestamp()
        )
    }

    fun syncEpg(force: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isEpgSyncing = true) }
            try {
                val currentChannels = _uiState.value.allChannels
                val updatedChannels = epgRepository.syncEpg(currentChannels, force = force)

                _uiState.update { current ->
                    val selected = current.selectedChannel
                    val updatedSelected = if (selected != null) {
                        updatedChannels.find { it.id == selected.id } ?: selected
                    } else null

                    current.copy(
                        allChannels = updatedChannels,
                        selectedChannel = updatedSelected,
                        isEpgSyncing = false,
                        lastEpgSyncTime = System.currentTimeMillis(),
                        snackbarMessage = if (force) "Guía EPG actualizada con programación oficial 📡" else current.snackbarMessage
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isEpgSyncing = false,
                        snackbarMessage = if (force) "No se pudo actualizar la guía EPG: ${e.message}" else null
                    )
                }
            }
        }
    }

    fun selectChannel(channel: Channel) {
        val updated = channel.copy(isFavorite = _uiState.value.favoriteIds.contains(channel.id))
        _uiState.update { it.copy(selectedChannel = updated) }
    }

    fun selectNextChannel() {
        val currentList = _uiState.value.filteredChannels.ifEmpty { _uiState.value.allChannels }
        val currentChannel = _uiState.value.selectedChannel ?: return
        val index = currentList.indexOfFirst { it.id == currentChannel.id }
        if (index != -1 && index + 1 < currentList.size) {
            selectChannel(currentList[index + 1])
        } else if (currentList.isNotEmpty()) {
            selectChannel(currentList.first())
        }
    }

    fun selectPreviousChannel() {
        val currentList = _uiState.value.filteredChannels.ifEmpty { _uiState.value.allChannels }
        val currentChannel = _uiState.value.selectedChannel ?: return
        val index = currentList.indexOfFirst { it.id == currentChannel.id }
        if (index > 0) {
            selectChannel(currentList[index - 1])
        } else if (currentList.isNotEmpty()) {
            selectChannel(currentList.last())
        }
    }

    fun setCountryFilter(country: Country?) {
        _uiState.update { current ->
            val newState = current.copy(selectedCountry = country)
            // If current channel doesn't match new filter, select the first matching one
            val matching = newState.filteredChannels
            val newSelected = if (country != null && current.selectedChannel?.country != country && matching.isNotEmpty()) {
                matching.first()
            } else current.selectedChannel
            newState.copy(selectedChannel = newSelected)
        }
    }

    fun setCategoryFilter(category: TvCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setOnlyFavorites(enabled: Boolean) {
        _uiState.update { it.copy(onlyFavorites = enabled) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setActiveTab(tab: AppTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun toggleFullscreen() {
        _uiState.update { it.copy(isFullscreen = !it.isFullscreen) }
    }

    fun setFullscreen(fullscreen: Boolean) {
        _uiState.update { it.copy(isFullscreen = fullscreen) }
    }

    fun toggleFavorite(channel: Channel) {
        val currentFavs = _uiState.value.favoriteIds.toMutableSet()
        val isNowFavorite: Boolean
        if (currentFavs.contains(channel.id)) {
            currentFavs.remove(channel.id)
            isNowFavorite = false
        } else {
            currentFavs.add(channel.id)
            isNowFavorite = true
        }

        prefs.edit().putStringSet("favorite_ids", currentFavs).apply()

        _uiState.update { state ->
            val updatedAll = state.allChannels.map {
                if (it.id == channel.id) it.copy(isFavorite = isNowFavorite) else it
            }
            val updatedSelected = if (state.selectedChannel?.id == channel.id) {
                state.selectedChannel.copy(isFavorite = isNowFavorite)
            } else state.selectedChannel

            state.copy(
                allChannels = updatedAll,
                selectedChannel = updatedSelected,
                favoriteIds = currentFavs,
                snackbarMessage = if (isNowFavorite) "${channel.name} agregado a favoritos ⭐" else "${channel.name} removido de favoritos"
            )
        }
    }

    fun toggleReminder(program: ProgramItem, channelName: String) {
        val currentReminders = _uiState.value.reminderIds.toMutableSet()
        val isNowReminder: Boolean
        if (currentReminders.contains(program.id)) {
            currentReminders.remove(program.id)
            isNowReminder = false
        } else {
            currentReminders.add(program.id)
            isNowReminder = true
        }

        prefs.edit().putStringSet("reminder_ids", currentReminders).apply()

        _uiState.update { state ->
            state.copy(
                reminderIds = currentReminders,
                snackbarMessage = if (isNowReminder) {
                    "🔔 Recordatorio fijado: '${program.title}' a las ${program.startTime} en $channelName"
                } else {
                    "Recordatorio cancelado para '${program.title}'"
                }
            )
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
