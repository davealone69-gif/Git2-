package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.OllamaClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OllamaScreen() {
    val scope = rememberCoroutineScope()
    val ollamaClient = remember { OllamaClient() }
    var prompt by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("Enter prompt to generate output.") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ollama Local LLM Generator") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = { Text("Prompt") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = {
                    if (prompt.isNotBlank()) {
                        isLoading = true
                        scope.launch {
                            val result = ollamaClient.generate(prompt)
                            isLoading = false
                            responseText = result.getOrElse { "Error: ${it.message}" }
                        }
                    }
                },
                enabled = !isLoading && prompt.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Generate Response")
                }
            }

            Text("Response:", style = MaterialTheme.typography.titleMedium)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(responseText, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
