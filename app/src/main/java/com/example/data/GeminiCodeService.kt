package com.example.data

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

/** Optional Gemini generator merged from GitHub-Boss. Never required for offline generation. */
class GeminiCodeService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .build()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val requestAdapter = moshi.adapter(GeminiRequest::class.java)
    private val responseAdapter = moshi.adapter(GeminiResponse::class.java)

    suspend fun generateKotlinCode(apiKey: String, prompt: String, packageName: String = "com.example"): ApiResult<String> {
        return try {
            val instructions = """
You are an expert Android Kotlin engineer. Generate production-ready Jetpack Compose + Material 3 code.
Package: $packageName
Output only repeated blocks in this format:
### FILE: path/to/File.kt
```kotlin
full file content
```
Request: $prompt
""".trimIndent()
            val body = GeminiRequest(
                contents = listOf(GeminiContent(listOf(GeminiPart(instructions)))),
                generationConfig = GenerationConfig(temperature = 0.35, maxOutputTokens = 8192)
            )
            val model = "gemini-2.0-flash"
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=${apiKey.trim()}"
            val request = Request.Builder()
                .url(url)
                .post(requestAdapter.toJson(body).toRequestBody("application/json".toMediaType()))
                .build()
            client.newCall(request).execute().use { response ->
                val raw = response.body?.string().orEmpty()
                if (!response.isSuccessful) return ApiResult.Error("Gemini error ${response.code}: ${raw.take(200)}", response.code)
                val text = responseAdapter.fromJson(raw)?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.trim()
                if (text.isNullOrBlank()) ApiResult.Error("Empty response from Gemini") else ApiResult.Success(text)
            }
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Gemini network error")
        }
    }

    companion object {
        fun parseGeminiOutput(raw: String): List<KotlinCodeMaker.KotlinFile> {
            val files = mutableListOf<KotlinCodeMaker.KotlinFile>()
            val regex = Regex("""###\s*FILE:\s*([^\n]+)\s*```(?:kotlin)?\s*([\s\S]*?)```""", RegexOption.IGNORE_CASE)
            regex.findAll(raw).forEach { match ->
                val path = match.groupValues[1].trim().removePrefix("/")
                val content = match.groupValues[2].trim()
                if (path.isNotBlank() && content.isNotBlank()) files += KotlinCodeMaker.KotlinFile(path, path.substringAfterLast('/'), content)
            }
            if (files.isEmpty() && raw.isNotBlank()) files += KotlinCodeMaker.KotlinFile("ui/GeneratedScreen.kt", "GeneratedScreen.kt", raw)
            return files
        }
    }
}

@JsonClass(generateAdapter = true)
data class GeminiRequest(val contents: List<GeminiContent>, val generationConfig: GenerationConfig? = null)
@JsonClass(generateAdapter = true)
data class GeminiContent(val parts: List<GeminiPart>, val role: String? = "user")
@JsonClass(generateAdapter = true)
data class GeminiPart(val text: String)
@JsonClass(generateAdapter = true)
data class GenerationConfig(val temperature: Double = 0.4, val maxOutputTokens: Int = 8192)
@JsonClass(generateAdapter = true)
data class GeminiResponse(val candidates: List<GeminiCandidate>?)
@JsonClass(generateAdapter = true)
data class GeminiCandidate(val content: GeminiContent?)
