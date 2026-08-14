package com.example.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsUiState(
    val darkTheme: Boolean = false,
    val autoSync: Boolean = true,
    val termuxScriptPath: String = "/sdcard/OMERA/scripts",
    val ollamaBaseUrl: String = "http://127.0.0.1:11434"
)

class SettingsViewModel : ViewModel() {
    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    fun setDarkTheme(enabled: Boolean) {
        _state.value = _state.value.copy(darkTheme = enabled)
    }

    fun setAutoSync(enabled: Boolean) {
        _state.value = _state.value.copy(autoSync = enabled)
    }

    fun setTermuxScriptPath(path: String) {
        _state.value = _state.value.copy(termuxScriptPath = path)
    }

    fun setOllamaBaseUrl(url: String) {
        _state.value = _state.value.copy(ollamaBaseUrl = url)
    }
}
