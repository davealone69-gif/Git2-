package com.example.data

/**
 * Result of a real build/verification phase. A successful report means the
 * specific phase completed; it does not imply later phases have succeeded.
 */
enum class BuildPhase {
    VERIFY,
    COMPLETE
}

data class BuildArtifact(
    val path: String,
    val type: String,
    val sizeBytes: Long,
    val sha256: String
)

data class BuildReport(
    val phase: BuildPhase,
    val success: Boolean,
    val message: String,
    val artifacts: List<BuildArtifact> = emptyList()
)
