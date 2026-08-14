package com.example.data

data class SystemInfoModel(
    val appVersion: String = "1.0.0",
    val buildType: String = "debug",
    val isOllamaAvailable: Boolean = false,
    val isTermuxAvailable: Boolean = true
)

data class TaskExecutionModel(
    val taskId: String,
    val command: String,
    val timestamp: Long,
    val status: String
)
