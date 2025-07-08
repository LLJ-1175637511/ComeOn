package com.google.media.lite_player.api.controller

interface IPlayerControllerAnimManager {
    fun showControllerAnim(useAnim: Boolean = true)

    fun hideControllerAnim(useAnim: Boolean = true)

    fun isControllerVisible(): Boolean
}