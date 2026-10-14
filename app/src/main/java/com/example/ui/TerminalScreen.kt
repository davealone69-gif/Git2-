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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen() {
    val context = LocalContext.current
    val termuxController = remember { com.example.termux.TermuxController(context) }
    val ollamaClient = remember { OllamaClient() }
    val scope = rememberCoroutineScope()
    var terminalOutput by remember { mutableStateOf("Termux Controller Initialized.\nReady for commands.\n") }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Termux Task Execution") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black)
                    .padding(12.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = terminalOutput,
                    color = Color.Green,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        termuxController.installUbuntu()
                        terminalOutput += "\n> Sent: installUbuntu()\n"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Install Ubuntu")
                }

                Button(
                    onClick = {
                        val result = termuxController.installOllama()
                        terminalOutput += "\n> " + result.fold(
                            { "Install command delivered to Termux. Confirm the package install in Termux output." },
                            { "Install command failed: ${it.message}" }
                        ) + "\n"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Install Ollama")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val result = termuxController.startOllama()
                        terminalOutput += "\n> " + result.fold(
                            { "Start command delivered. Checking whether Ollama API responds..." },
                            { "Start command failed: ${it.message}" }
                        ) + "\n"
                        if (result.isSuccess) scope.launch {
                            var ready = false
                            repeat(20) {
                                delay(1000)
                                if (ollamaClient.health().isSuccess) {
                                    ready = true
                                    terminalOutput += "\n> SERVER RESPONDS at 127.0.0.1:11434. Chat completion is not verified until a real prompt succeeds.\n"
                                    return@launch
                                }
                            }
                            if (!ready) terminalOutput += "\n> SERVER NOT VERIFIED: no successful API health response within 20 seconds. Check ~/ollama.log and Termux permission/settings.\n"
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Start Ollama")
                }

                Button(
                    onClick = {
                        termuxController.pullModel("llama3.1")
                        terminalOutput += "\n> Sent: pullModel(llama3.1)\n"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Pull Llama3.1")
                }
            }
        }
    }
}
