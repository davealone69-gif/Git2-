package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "OMERA Control Panel",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { /* TODO: Hook TermuxController */ }) {
            Text("Run Termux Task")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* TODO: Hook GitHubClient */ }) {
            Text("GitHub Action")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* TODO: Hook OllamaClient */ }) {
            Text("Generate with Ollama")
        }
    }
}
