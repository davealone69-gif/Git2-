package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.termux.TermuxController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TermuxUiState(
    val logs: String = "Termux Task Controller ready.\n",
    val isRunning: Boolean = false
)

class TermuxViewModel : ViewModel() {
    private val _state = MutableStateFlow(TermuxUiState())
    val state: StateFlow<TermuxUiState> = _state.asStateFlow()

    fun runUbuntuInstall(context: Context) {
        val controller = TermuxController(context)
        appendLog("Initiating proot Ubuntu installation...")
        controller.installUbuntu()
        appendLog("Ubuntu installation script dispatched.")
    }

    fun runOllamaInstall(context: Context) {
        val controller = TermuxController(context)
        appendLog("Initiating Ollama setup in Ubuntu...")
        controller.installOllama()
        appendLog("Ollama setup script dispatched.")
    }

    fun runOllamaStart(context: Context) {
        val controller = TermuxController(context)
        appendLog("Starting Ollama service...")
        controller.startOllama()
        appendLog("Ollama service start command dispatched.")
    }

    fun pullModel(context: Context, model: String = "llama3.1") {
        val controller = TermuxController(context)
        appendLog("Pulling model $model...")
        controller.pullModel(model)
        appendLog("Pull script for $model dispatched.")
    }

    fun appendLog(message: String) {
        _state.value = _state.value.copy(
            logs = _state.value.logs + "\n> " + message
        )
    }

    fun clearLogs() {
        _state.value = _state.value.copy(logs = "")
    }
}
