package com.google.media.lite_player.api.controller

import androidx.media3.common.Player

/**
 * @author liulinjie 
 * @since 2025-07-09
 */
interface IPlayerController {
    fun registerControllerCallbacks(controllerActionListener: ControllerActionListener)

    fun unregisterControllerCallbacks(controllerActionListener: ControllerActionListener)

    fun showController()

    fun hideController()

    fun release()
        
    fun setPlayer(player: Player?)
}