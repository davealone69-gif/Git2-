package com.example.utils

import java.net.HttpURLConnection
import java.net.URL

object NetworkUtils {
    fun pingUrl(urlString: String, timeoutMs: Int = 3000): Boolean {
        return try {
            val url = URL(urlString)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = timeoutMs
            conn.readTimeout = timeoutMs
            conn.requestMethod = "HEAD"
            val responseCode = conn.responseCode
            conn.disconnect()
            responseCode in 200..399
        } catch (e: Exception) {
            false
        }
    }
}
