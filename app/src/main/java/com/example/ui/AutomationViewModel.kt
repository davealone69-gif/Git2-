package com.example.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AutomationUiState(
    val autoFixerActive: Boolean = true,
    val autoBackupActive: Boolean = false,
    val lastSyncTime: String = "Never",
    val statusLog: List<String> = emptyList()
)

class AutomationViewModel : ViewModel() {
    private val _state = MutableStateFlow(AutomationUiState())
    val state: StateFlow<AutomationUiState> = _state.asStateFlow()

    fun toggleAutoFixer(enabled: Boolean) {
        _state.value = _state.value.copy(autoFixerActive = enabled)
        logEvent("AutoFixer set to $enabled")
    }

    fun toggleAutoBackup(enabled: Boolean) {
        _state.value = _state.value.copy(autoBackupActive = enabled)
        logEvent("AutoBackup set to $enabled")
    }

    fun triggerSync() {
        val currentTime = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        _state.value = _state.value.copy(lastSyncTime = currentTime)
        logEvent("Manual sync triggered at $currentTime")
    }

    private fun logEvent(event: String) {
        val currentLogs = _state.value.statusLog.toMutableList()
        currentLogs.add(0, event)
        _state.value = _state.value.copy(statusLog = currentLogs)
    }
}
