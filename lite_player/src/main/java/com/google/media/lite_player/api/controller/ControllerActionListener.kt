package com.google.media.lite_player.api.controller

import com.google.media.lite_player.ui.ControllerAction

fun interface ControllerActionListener {
    fun onAction(action: ControllerAction)
}