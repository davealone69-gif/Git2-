package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utils.SystemUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DiagnosticsUiState(
    val memoryUsage: String = "Checking...",
    val isNetworkConnected: Boolean = false,
    val isStorageAccessible: Boolean = true,
    val activeIssuesCount: Int = 0
)

class DiagnosticsViewModel : ViewModel() {
    private val _state = MutableStateFlow(DiagnosticsUiState())
    val state: StateFlow<DiagnosticsUiState> = _state.asStateFlow()

    fun refreshDiagnostics(context: Context) {
        viewModelScope.launch {
            val mem = SystemUtils.getMemoryInfo(context)
            val net = SystemUtils.isNetworkAvailable(context)
            _state.value = DiagnosticsUiState(
                memoryUsage = mem,
                isNetworkConnected = net,
                isStorageAccessible = true,
                activeIssuesCount = if (!net) 1 else 0
            )
        }
    }
}
