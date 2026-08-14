package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.OllamaClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface OllamaUiState {
    object Idle : OllamaUiState
    object Loading : OllamaUiState
    data class Success(val response: String) : OllamaUiState
    data class Error(val message: String) : OllamaUiState
}

class OllamaViewModel(
    private val client: OllamaClient = OllamaClient()
) : ViewModel() {

    private val _uiState = MutableStateFlow<OllamaUiState>(OllamaUiState.Idle)
    val uiState: StateFlow<OllamaUiState> = _uiState.asStateFlow()

    fun generate(prompt: String, model: String = "llama3.1") {
        if (prompt.isBlank()) return
        _uiState.value = OllamaUiState.Loading
        viewModelScope.launch {
            val result = client.generate(prompt, model)
            result.fold(
                onSuccess = { _uiState.value = OllamaUiState.Success(it) },
                onFailure = { _uiState.value = OllamaUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}
