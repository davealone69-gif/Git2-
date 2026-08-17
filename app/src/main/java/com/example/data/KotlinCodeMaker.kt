package com.example.data

/** Offline deterministic Kotlin/Compose generator merged from GitHub-Boss. */
object KotlinCodeMaker {
    data class KotlinFile(val path: String, val name: String, val content: String)

    fun generate(prompt: String, packageName: String = "com.example"): List<KotlinFile> {
        val clean = prompt.trim().ifBlank { "Simple demo screen" }
        val slug = clean.lowercase()
            .replace(Regex("[^a-z0-9]+"), " ").trim()
            .split(" ").filter { it.isNotBlank() }.take(4)
            .joinToString("") { it.replaceFirstChar { c -> c.uppercase() } }
            .ifBlank { "Demo" }
        val className = if (slug.endsWith("Screen") || slug.endsWith("View")) slug else "${slug}Screen"
        val viewModelName = className.removeSuffix("Screen") + "ViewModel"
        val stateName = className.removeSuffix("Screen") + "UiState"
        val wantsList = clean.contains("list", true) || clean.contains("feed", true) || clean.contains("items", true)
        val wantsForm = clean.contains("form", true) || clean.contains("input", true) || clean.contains("login", true)
        val files = mutableListOf<KotlinFile>()
        files += KotlinFile("ui/$stateName.kt", "$stateName.kt", """
package $packageName.ui

data class $stateName(
    val isLoading: Boolean = false,
    val error: String? = null,
    val title: String = "$clean",
    ${if (wantsList) "val items: List<String> = emptyList()," else ""}
    ${if (wantsForm) "val inputText: String = \"\"," else ""}
    val message: String = "Ready"
)
""".trimIndent())
        files += KotlinFile("ui/$viewModelName.kt", "$viewModelName.kt", """
package $packageName.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class $viewModelName : ViewModel() {
    private val _uiState = MutableStateFlow($stateName())
    val uiState: StateFlow<$stateName> = _uiState.asStateFlow()
    fun refresh() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        delay(400)
        _uiState.update { it.copy(isLoading = false, message = "Loaded successfully"${if (wantsList) ", items = listOf(\"Item 1\", \"Item 2\", \"Item 3\")" else ""}) }
    }
    ${if (wantsForm) "fun onInputChange(value: String) { _uiState.update { it.copy(inputText = value) } }" else ""}
}
""".trimIndent())
        files += KotlinFile("ui/$className.kt", "$className.kt", """
package $packageName.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun $className(viewModel: $viewModelName, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.refresh() }
    Column(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(state.title, style = MaterialTheme.typography.titleLarge)
        if (state.isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Text(state.message)
        ${if (wantsForm) "OutlinedTextField(value = state.inputText, onValueChange = viewModel::onInputChange, label = { Text(\"Input\") }, modifier = Modifier.fillMaxWidth())" else ""}
        Button(onClick = { viewModel.refresh() }, modifier = Modifier.fillMaxWidth()) { Text("Refresh") }
    }
}
""".trimIndent())
        return files
    }
}
