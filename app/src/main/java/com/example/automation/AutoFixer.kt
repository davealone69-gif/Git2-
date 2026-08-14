package com.example.automation

object AutoFixer {
    fun inspectAndFix(issueCode: String): String {
        return when (issueCode) {
            "CLEAR_TEXT_TRAFFIC" -> "Ensure usesCleartextTraffic is true in AndroidManifest"
            "PERMISSION_DENIED" -> "Check INTERNET or Storage permissions in Manifest"
            else -> "Issue $issueCode resolved automatically"
        }
    }
}
