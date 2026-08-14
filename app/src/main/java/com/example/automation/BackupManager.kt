package com.example.automation

import com.example.utils.FileUtils
import com.example.utils.Logger

object BackupManager {
    fun backupScript(scriptPath: String, backupDir: String = "/sdcard/OMERA/backups"): Boolean {
        val content = FileUtils.readTextFile(scriptPath) ?: return false
        val filename = scriptPath.substringAfterLast("/")
        val destPath = "$backupDir/$filename.bak"
        val success = FileUtils.writeTextFile(destPath, content)
        if (success) {
            Logger.i("Backup successful for $filename to $destPath")
        } else {
            Logger.e("Backup failed for $filename")
        }
        return success
    }
}
