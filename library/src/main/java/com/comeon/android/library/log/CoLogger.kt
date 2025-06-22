package com.comeon.android.library.log

import android.util.Log

class CoLogger(private val module: String) : ILogger {
    override fun d(tag: String, message: String) {
        Log.d(module, mergeTagAndMessage(tag, message))
    }

    override fun i(tag: String, message: String) {
        Log.i(module, mergeTagAndMessage(tag, message))
    }

    override fun w(tag: String, message: String) {
        Log.w(module, mergeTagAndMessage(tag, message))
    }

    override fun e(tag: String, message: String) {
        Log.e(module, mergeTagAndMessage(tag, message))
    }

    override fun e(tag: String, throwable: Throwable) {
        Log.e(module, mergeTagAndMessage(tag, throwable.message ?: "Unknown error"))
    }

    private fun mergeTagAndMessage(tag: String, message: String): String {
        return "[$tag]: $message"
    }

}