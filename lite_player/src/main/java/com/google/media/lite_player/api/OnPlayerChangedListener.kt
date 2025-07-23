package com.google.media.lite_player.api

import androidx.media3.common.Player

fun interface OnPlayerChangedListener {
    fun onPlayerChangedListener(oldPlayer: Player?, newPlayer: Player?)
}