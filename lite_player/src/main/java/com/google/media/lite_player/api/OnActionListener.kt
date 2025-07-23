package com.google.media.lite_player.api

fun interface OnActionListener<T> {
    fun onAction(action: T)
}