package com.example.data

data class SourceCapability(
    val source: String,
    val capability: String,
    val integration: String,
    val licenseStatus: String
)

object SourceFusionCatalog {
    val capabilities = listOf(
        SourceCapability("srp_app_builder", "clone/split/assemble/compile/index", "Builder pipeline", "inspect-before-copy"),
        SourceCapability("srp-app-builder-web", "GitHub discovery and symbol indexing", "Project inspector", "inspect-before-copy"),
        SourceCapability("pyforge", "AI analysis -> isolated build -> live logs -> artifact", "Build pipeline", "MIT"),
        SourceCapability("agent-zero", "projects/tools/skills/memory/agent cooperation", "OMERA agent layer", "inspect-before-copy"),
        SourceCapability("agent-zero-kali", "bootstrap and boot service", "Termux/server lifecycle", "inspect-before-copy"),
        SourceCapability("a0-custom", "runtime reuse, instances, validation contract", "Local runtime manager", "MIT"),
        SourceCapability("Nvidia-Agent-Core", "ReAct, tools, sessions, JSONL logging", "Local-agent adapter", "STAR-WARE, no source copy"),
        SourceCapability("sovereign-ula", "Android/Linux shell and service concepts", "Device environment", "inspect-before-copy"),
        SourceCapability("Project_Chimera", "separate project architecture", "Audit only", "inspect-before-copy"),
        SourceCapability("fantastic-robot", "large auxiliary project", "Audit tooling", "inspect-before-copy"),
        SourceCapability("A0k", "Agent Zero derivative", "Audit delta", "inspect-before-copy"),
        SourceCapability("K", "auxiliary repository", "Audit tooling", "inspect-before-copy")
    )
}
