package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.data.OllamaClient
import com.example.termux.TermuxController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen() {
    val context = LocalContext.current
    val termuxController = remember { TermuxController(context) }
    val ollamaClient = remember { OllamaClient() }
    val scope = rememberCoroutineScope()
    var terminalOutput by remember { mutableStateOf("Termux Controller Initialized.\nReady for commands.\n") }
    val scrollState = rememberScrollState()

    Scaffold(topBar = { TopAppBar(title = { Text("Termux Task Execution") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Black).padding(12.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = terminalOutput,
                    color = Color.Green,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val result = termuxController.installUbuntu()
                        terminalOutput += "\n> " + result.fold(
                            { "Ubuntu install command delivered. Completion must be checked in Termux." },
                            { "Ubuntu install command failed: ${it.message}" }
                        ) + "\n"
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Install Ubuntu") }

                Button(
                    onClick = {
                        val result = termuxController.installOllama()
                        terminalOutput += "\n> " + result.fold(
                            { "Ollama package install command delivered. This is not proof installation completed." },
                            { "Ollama install command failed: ${it.message}" }
                        ) + "\n"
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Install Ollama") }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val result = termuxController.startOllama()
                        terminalOutput += "\n> " + result.fold(
                            { "Start command delivered. Checking whether the Ollama API responds..." },
                            { "Start command failed: ${it.message}" }
                        ) + "\n"
                        if (result.isSuccess) scope.launch {
                            var responding = false
                            repeat(20) {
                                delay(1000)
                                if (ollamaClient.health().isSuccess) {
                                    responding = true
                                    terminalOutput += "\n> SERVER RESPONDS at 127.0.0.1:11434. Chat completion remains unverified until a real prompt succeeds.\n"
                                    return@launch
                                }
                            }
                            if (!responding) {
                                terminalOutput += "\n> SERVER NOT VERIFIED: no successful API health response within 20 seconds. Check ~/ollama.log, Termux permission, and allow-external-apps.\n"
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Start Ollama") }

                Button(
                    onClick = {
                        val result = termuxController.pullModel("llama3.2:1b")
                        terminalOutput += "\n> " + result.fold(
                            { "Model pull command delivered for llama3.2:1b. Verify it appears in /api/tags before using it." },
                            { "Model pull command failed: ${it.message}" }
                        ) + "\n"
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Pull llama3.2:1b") }
            }
        }
    }
}
