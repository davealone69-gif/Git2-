package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class OllamaClient(
    private val baseUrl: String = "http://127.0.0.1:11434"
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generate(prompt: String, model: String = "llama3.1"): Result<String> = withContext(Dispatchers.IO) {
        try {
            val jsonPayload = JSONObject().apply {
                put("model", model)
                put("prompt", prompt)
                put("stream", false)
            }.toString()

            val request = Request.Builder()
                .url("$baseUrl/api/generate")
                .post(jsonPayload.toRequestBody(jsonMediaType))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("HTTP ${response.code}: ${response.message}"))
                }
                val bodyString = response.body?.string() ?: throw Exception("Empty response body")
                val jsonResponse = JSONObject(bodyString)
                val responseText = jsonResponse.optString("response", "No content generated")
                Result.success(responseText)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
