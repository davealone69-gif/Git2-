package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToTerminal: () -> Unit = {},
    onNavigateToOllama: () -> Unit = {},
    onNavigateToGitHub: () -> Unit = {},
    onNavigateToDiagnostics: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("OMERA Dashboard") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("System Status", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("OMERA Core: Active", style = MaterialTheme.typography.bodyMedium)
                    Text("Termux Bridge: Ready", style = MaterialTheme.typography.bodyMedium)
                    Text("Ollama LLM Engine: Standby", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Button(
                onClick = onNavigateToTerminal,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open Termux Terminal Controller")
            }

            Button(
                onClick = onNavigateToOllama,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Launch Ollama AI Studio")
            }

            Button(
                onClick = onNavigateToGitHub,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Manage GitHub Repositories")
            }

            OutlinedButton(
                onClick = onNavigateToDiagnostics,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("System Diagnostics & Health")
            }
        }
    }
}
