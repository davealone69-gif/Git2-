package com.example.diagnostics

object LogCollector {
    private val logs = mutableListOf<String>()

    fun log(tag: String, message: String) {
        val entry = "[${System.currentTimeMillis()}] $tag: $message"
        synchronized(logs) {
            logs.add(entry)
            if (logs.size > 500) {
                logs.removeAt(0)
            }
        }
    }

    fun getAllLogs(): List<String> {
        return synchronized(logs) {
            logs.toList()
        }
    }

    fun clear() {
        synchronized(logs) {
            logs.clear()
        }
    }
}
