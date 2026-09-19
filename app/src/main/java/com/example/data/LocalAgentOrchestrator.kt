package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalAgentOrchestrator(
    private val client: OllamaClient = OllamaClient()
) {
    data class AgentResult(val response: String, val steps: List<String>)

    suspend fun run(
        task: String,
        model: String = "llama3.2:1b",
        maxSteps: Int = 3
    ): Result<AgentResult> = withContext(Dispatchers.IO) {
        if (task.isBlank()) return@withContext Result.failure(IllegalArgumentException("Task cannot be blank"))

        val steps = mutableListOf<String>()
        var context = task

        repeat(maxSteps.coerceIn(1, 6)) { index ->
            steps += "STEP ${index + 1}: local model"
            val prompt = """
                You are the local planning agent for an Android build/control app.
                Work only with information supplied in the task.
                If an external action is unavailable, say so clearly.
                Prefer a concrete next action over vague advice.

                Task:
                $context
            """.trimIndent()

            val result = client.generate(prompt, model)
            if (result.isFailure) {
                return@withContext Result.failure(
                    result.exceptionOrNull() ?: IllegalStateException("Local model failed")
                )
            }

            val answer = result.getOrThrow()
            context = answer
            if (!answer.contains("NEXT:", ignoreCase = true)) {
                return@withContext Result.success(AgentResult(answer, steps))
            }
        }

        Result.success(AgentResult(context, steps))
    }
}
