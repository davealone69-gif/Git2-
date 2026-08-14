package com.example.utils

import android.util.Log

object Logger {
    private const val DEFAULT_TAG = "OMERA_SYSTEM"

    fun d(message: String, tag: String = DEFAULT_TAG) {
        Log.d(tag, message)
    }

    fun i(message: String, tag: String = DEFAULT_TAG) {
        Log.i(tag, message)
    }

    fun w(message: String, tag: String = DEFAULT_TAG) {
        Log.w(tag, message)
    }

    fun e(message: String, throwable: Throwable? = null, tag: String = DEFAULT_TAG) {
        Log.e(tag, message, throwable)
    }
}
