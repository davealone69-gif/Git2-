package com.example.omera

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class OmeraSystemState(
    val isInitialized: Boolean = false,
    val isTermuxReady: Boolean = false,
    val isOllamaOnline: Boolean = false,
    val activeTaskCount: Int = 0
)

class OmeraEngine {
    private val _state = MutableStateFlow(OmeraSystemState())
    val state: StateFlow<OmeraSystemState> = _state.asStateFlow()

    fun initialize() {
        _state.value = _state.value.copy(
            isInitialized = true,
            isTermuxReady = true
        )
    }

    fun updateOllamaStatus(online: Boolean) {
        _state.value = _state.value.copy(isOllamaOnline = online)
    }

    fun incrementTasks() {
        _state.value = _state.value.copy(activeTaskCount = _state.value.activeTaskCount + 1)
    }

    fun decrementTasks() {
        if (_state.value.activeTaskCount > 0) {
            _state.value = _state.value.copy(activeTaskCount = _state.value.activeTaskCount - 1)
        }
    }
}
