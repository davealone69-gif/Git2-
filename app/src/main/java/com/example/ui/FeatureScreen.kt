package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureScreen(items: List<String>, onAdd: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feature Screen") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(items) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(item, style = MaterialTheme.typography.bodyLarge)
                    HorizontalDivider()
                }
            }
        }
    }
}
