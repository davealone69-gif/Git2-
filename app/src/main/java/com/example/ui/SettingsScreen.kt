package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var isDarkTheme by remember { mutableStateOf(false) }
    var autoSyncEnabled by remember { mutableStateOf(true) }
    var termuxPath by remember { mutableStateOf("/sdcard/OMERA/scripts") }
    var ollamaUrl by remember { mutableStateOf("http://127.0.0.1:11434") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings & Configuration") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("OMERA System Preferences", style = MaterialTheme.typography.titleMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dark Theme")
                Switch(checked = isDarkTheme, onCheckedChange = { isDarkTheme = it })
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enable Auto-Sync")
                Switch(checked = autoSyncEnabled, onCheckedChange = { autoSyncEnabled = it })
            }

            Divider()

            Text("Environment Configurations", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = termuxPath,
                onValueChange = { termuxPath = it },
                label = { Text("Termux Script Path") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ollamaUrl,
                onValueChange = { ollamaUrl = it },
                label = { Text("Ollama Base URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { /* Save Settings */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save Configuration")
            }
        }
    }
}
