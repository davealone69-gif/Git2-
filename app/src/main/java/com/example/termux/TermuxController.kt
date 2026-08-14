package com.example.termux

import android.content.Context
import android.content.Intent
import java.io.File

class TermuxController(private val context: Context) {

    private val scriptDir = File("/sdcard/OMERA/scripts").apply {
        if (!exists()) mkdirs()
    }

    private fun writeScript(name: String, content: String): File {
        val file = File(scriptDir, name)
        file.writeText(content)
        return file
    }

    fun runScript(script: File) {
        val intent = Intent().apply {
            action = "com.termux.RUN_COMMAND"
            setClassName("com.termux", "com.termux.app.RunCommandService")
            putExtra("com.termux.RUN_COMMAND_PATH", script.absolutePath)
            putExtra("com.termux.RUN_COMMAND_BACKGROUND", true)
        }
        context.startService(intent)
    }

    // ---------------------------------------------------------
    // 1. Install proot-distro + Ubuntu
    // ---------------------------------------------------------
    fun installUbuntu() {
        val script = writeScript(
            "install_ubuntu.sh",
            """
            pkg update -y
            pkg install -y proot-distro wget curl tar
            proot-distro install ubuntu
            """.trimIndent()
        )
        runScript(script)
    }

    // ---------------------------------------------------------
    // 2. Launch Ubuntu shell
    // ---------------------------------------------------------
    fun enterUbuntu() {
        val script = writeScript(
            "enter_ubuntu.sh",
            """
            proot-distro login ubuntu
            """.trimIndent()
        )
        runScript(script)
    }

    // ---------------------------------------------------------
    // 3. Install Ollama inside Ubuntu
    // ---------------------------------------------------------
    fun installOllama() {
        val script = writeScript(
            "install_ollama.sh",
            """
            proot-distro login ubuntu -- bash -c "
                curl -fsSL https://ollama.com/install.sh | sh
            "
            """.trimIndent()
        )
        runScript(script)
    }

    // ---------------------------------------------------------
    // 4. Pull a model (llama3.1 by default)
    // ---------------------------------------------------------
    fun pullModel(model: String = "llama3.1") {
        val script = writeScript(
            "pull_model.sh",
            """
            proot-distro login ubuntu -- ollama pull $model
            """.trimIndent()
        )
        runScript(script)
    }

    // ---------------------------------------------------------
    // 5. Start Ollama server
    // ---------------------------------------------------------
    fun startOllama() {
        val script = writeScript(
            "start_ollama.sh",
            """
            proot-distro login ubuntu -- ollama serve
            """.trimIndent()
        )
        runScript(script)
    }

    // ---------------------------------------------------------
    // 6. Build an Android project inside Ubuntu
    // ---------------------------------------------------------
    fun buildAndroidProject(projectPath: String) {
        val script = writeScript(
            "build_android.sh",
            """
            proot-distro login ubuntu -- bash -c "
                cd $projectPath
                ./gradlew assembleDebug
            "
            """.trimIndent()
        )
        runScript(script)
    }
}
