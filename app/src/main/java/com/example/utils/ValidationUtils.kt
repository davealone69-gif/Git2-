package com.example.utils

object ValidationUtils {
    fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }

    fun isValidFilePath(path: String): Boolean {
        return path.startsWith("/")
    }
}
