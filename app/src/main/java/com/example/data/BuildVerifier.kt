package com.example.data

import java.io.File
import java.security.MessageDigest

object BuildVerifier {
    fun verifyApk(apk: File): BuildReport {
        if (!apk.exists()) return BuildReport(BuildPhase.VERIFY, false, "APK missing: ${apk.absolutePath}")
        if (!apk.isFile || apk.length() <= 0L) return BuildReport(BuildPhase.VERIFY, false, "APK is not a non-empty file: ${apk.absolutePath}")

        return BuildReport(
            BuildPhase.COMPLETE,
            true,
            "APK verified: ${apk.name} (${apk.length()} bytes)",
            listOf(BuildArtifact(apk.absolutePath, "APK", apk.length(), sha256(apk)))
        )
    }

    private fun sha256(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            while (true) {
                val read = input.read(buffer)
                if (read <= 0) break
                digest.update(buffer, 0, read)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }
}
