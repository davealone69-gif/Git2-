package com.example.utils

import org.json.JSONObject

object JsonUtils {
    fun parseKey(jsonString: String, key: String): String? {
        return try {
            val json = JSONObject(jsonString)
            json.optString(key, null)
        } catch (e: Exception) {
            null
        }
    }

    fun isValidJson(jsonString: String): Boolean {
        return try {
            JSONObject(jsonString)
            true
        } catch (e: Exception) {
            false
        }
    }
}
