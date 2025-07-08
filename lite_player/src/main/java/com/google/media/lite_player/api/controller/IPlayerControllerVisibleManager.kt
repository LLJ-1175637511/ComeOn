package com.google.media.lite_player.api.controller

interface IPlayerControllerVisibleManager {
    fun showControllerAnim(useAnim: Boolean = true)

    fun hideControllerAnim(useAnim: Boolean = true)

    fun isControllerVisible(): Boolean

    fun registerControllerVisibleListener(listener: OnViewVisibleListener)

    fun unregisterControllerVisibleListener(listener: OnViewVisibleListener)
}