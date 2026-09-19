package com.example.data

enum class BuildPhase {
    INSPECT, PLAN, ASSEMBLE, BUILD, VERIFY, COMPLETE, FAILED
}

data class BuildArtifact(
    val path: String,
    val kind: String,
    val sizeBytes: Long? = null,
    val sha256: String? = null
)

data class BuildReport(
    val phase: BuildPhase,
    val success: Boolean,
    val message: String,
    val artifacts: List<BuildArtifact> = emptyList()
)

data class SourceCapability(
    val name: String,
    val capability: String,
    val sourceProject: String,
    val integrationTarget: String
)

object SourceFusionCatalog {
    val capabilities = listOf(
        SourceCapability("SRP decomposition", "Repository inspection and component-oriented assembly", "princessesnugglebunny-spec/srp_app_builder", "Git2 Builder"),
        SourceCapability("Build pipeline", "Explicit build stages, logs and artifact handling", "princessesnugglebunny-spec/pyforge", "Git2 APK pipeline"),
        SourceCapability("Agent loop", "Planner/tool/observation iteration with persistent state", "princessesnugglebunny-spec/Nvidia-Agent-Core", "Git2 local AI"),
        SourceCapability("Project + skills model", "Isolated project context and extensible tools", "princessesnugglebunny-spec/agent-zero", "OMERA"),
        SourceCapability("Server bootstrap", "Repeatable local server startup and recovery", "princessesnugglebunny-spec/agent-zero-kali", "Termux/Ollama")
    )
}
