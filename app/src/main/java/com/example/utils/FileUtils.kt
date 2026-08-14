package com.example.utils

import java.io.File

object FileUtils {
    fun createDirIfNotExists(dirPath: String): File {
        val dir = File(dirPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    fun writeTextFile(filePath: String, content: String): Boolean {
        return try {
            val file = File(filePath)
            file.parentFile?.mkdirs()
            file.writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun readTextFile(filePath: String): String? {
        return try {
            val file = File(filePath)
            if (file.exists()) file.readText() else null
        } catch (e: Exception) {
            null
        }
    }
}
