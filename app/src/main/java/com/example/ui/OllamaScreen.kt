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
    var serverStatus by remember { mutableStateOf("Not checked. Server health does not prove chat generation works.") }
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

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    enabled = !isLoading,
                    onClick = {
                        scope.launch {
                            serverStatus = "Checking Ollama API..."
                            ollamaClient.health().fold(
                                onSuccess = { serverStatus = "SERVER RESPONDS · chat completion is not verified until Generate Response succeeds." },
                                onFailure = { serverStatus = "SERVER UNAVAILABLE · ${it.message ?: "connection failed"}" }
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Check Server") }
                Button(
                    onClick = {
                        if (prompt.isNotBlank()) {
                        isLoading = true
                        scope.launch {
                            val result = ollamaClient.generate(prompt)
                            isLoading = false
                            responseText = result.fold(
                                onSuccess = { "CHAT VERIFIED · a real Ollama generation returned a non-empty response.\n\n" + it },
                                onFailure = { "CHAT FAILED · ${it.message ?: "unknown error"}" }
                            )
                        }
                    }
                },
                enabled = !isLoading && prompt.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Generate Response")
                }
            }

            Text("Server status: $serverStatus", style = MaterialTheme.typography.bodySmall)
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
