package com.example.termux

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class TermuxController(private val context: Context) {
    private val bashPath = "/data/data/com.termux/files/usr/bin/bash"

    fun isInstalled(): Boolean =
        try {
            context.packageManager.getPackageInfo("com.termux", 0)
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }

    fun runCommand(command: String, workDir: String? = null): Result<Unit> {
        if (!isInstalled()) return Result.failure(IllegalStateException("Termux is not installed"))

        return try {
            val intent = Intent().apply {
                action = "com.termux.RUN_COMMAND"
                setClassName("com.termux", "com.termux.app.RunCommandService")
                putExtra("com.termux.RUN_COMMAND_PATH", bashPath)
                putExtra("com.termux.RUN_COMMAND_ARGUMENTS", arrayOf("-lc", command))
                putExtra("com.termux.RUN_COMMAND_BACKGROUND", true)
                if (!workDir.isNullOrBlank()) putExtra("com.termux.RUN_COMMAND_WORKDIR", workDir)
            }
            context.startService(intent)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun startOllamaServer(): Result<Unit> =
        runCommand("if ! pgrep -f '(^| )ollama serve( |$)' >/dev/null 2>&1; then nohup ollama serve >/data/data/com.termux/files/home/ollama.log 2>&1 & fi")

    fun ensureLocalServer(): Result<Unit> =
        runCommand("if command -v ollama >/dev/null 2>&1; then if ! pgrep -f '(^| )ollama serve( |$)' >/dev/null 2>&1; then nohup ollama serve >/data/data/com.termux/files/home/ollama.log 2>&1 & fi; else echo 'Ollama is not installed'; exit 127; fi")

    fun installUbuntu(): Result<Unit> =
        runCommand("pkg update -y && pkg install -y proot-distro wget curl tar && proot-distro install ubuntu")

    fun enterUbuntu(): Result<Unit> = runCommand("proot-distro login ubuntu")

    fun installOllama(): Result<Unit> =
        runCommand("proot-distro login ubuntu -- bash -lc 'curl -fsSL https://ollama.com/install.sh | sh'")

    fun pullModel(model: String = "llama3.2:1b"): Result<Unit> =
        runCommand("ollama pull ${shellQuote(model)}")

    fun buildAndroidProject(projectPath: String): Result<Unit> =
        runCommand("cd ${shellQuote(projectPath)} && test -x ./gradlew && ./gradlew assembleDebug --no-daemon", projectPath)

    private fun shellQuote(value: String): String = "'" + value.replace("'", "'\\''") + "'"
}
