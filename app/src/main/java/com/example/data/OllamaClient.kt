package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class OllamaClient(private val baseUrl: String = "http://127.0.0.1:11434") {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun health(): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            client.newCall(Request.Builder().url("$baseUrl/api/tags").get().build()).execute().use { r ->
                if (r.isSuccessful) Result.success(true)
                else Result.failure(Exception("Ollama HTTP ${r.code}: ${r.message}"))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun generate(prompt: String, model: String = "llama3.2:1b"): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val payload = JSONObject().apply {
                    put("model", model)
                    put("prompt", prompt)
                    put("stream", false)
                }.toString()
                val request = Request.Builder()
                    .url("$baseUrl/api/generate")
                    .post(payload.toRequestBody(jsonMediaType))
                    .build()
                client.newCall(request).execute().use { r ->
                    if (!r.isSuccessful) {
                        return@withContext Result.failure(Exception("Ollama HTTP ${r.code}: ${r.message}"))
                    }
                    val text = JSONObject(r.body?.string() ?: "").optString("response")
                    if (text.isBlank()) Result.failure(Exception("Ollama returned an empty response"))
                    else Result.success(text)
                }
            } catch (e: Exception) { Result.failure(e) }
        }
}
