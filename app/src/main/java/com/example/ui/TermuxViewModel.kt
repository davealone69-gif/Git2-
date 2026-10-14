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
        controller.installUbuntu().fold(
            { appendLog("Ubuntu install command delivered. Check Termux output for actual completion.") },
            { appendLog("Ubuntu install command failed: ${it.message}") }
        )
    }

    fun runOllamaInstall(context: Context) {
        val controller = TermuxController(context)
        appendLog("Installing Ollama in the Termux environment...")
        controller.installOllama().fold(
            { appendLog("Ollama package install command delivered. Confirm installation in Termux output.") },
            { appendLog("Ollama install command failed: ${it.message}") }
        )
    }

    fun runOllamaStart(context: Context) {
        val controller = TermuxController(context)
        appendLog("Requesting Ollama startup in Termux...")
        controller.startOllama().fold(
            { appendLog("Start command delivered. Server readiness and real chat completion still require separate checks.") },
            { appendLog("Ollama start command failed: ${it.message}") }
        )
    }

    fun pullModel(context: Context, model: String = "llama3.2:1b") {
        val controller = TermuxController(context)
        appendLog("Requesting model pull for $model...")
        controller.pullModel(model).fold(
            { appendLog("Model pull command delivered. Confirm the model appears in Ollama's model list.") },
            { appendLog("Model pull command failed: ${it.message}") }
        )
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
