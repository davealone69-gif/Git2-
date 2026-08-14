package com.example.diagnostics

import android.content.Context
import com.example.utils.SystemUtils

object HealthMonitor {
    fun getHealthReport(context: Context): Map<String, Any> {
        return mapOf(
            "memory" to SystemUtils.getMemoryInfo(context),
            "network" to SystemUtils.isNetworkAvailable(context),
            "timestamp" to System.currentTimeMillis()
        )
    }
}
