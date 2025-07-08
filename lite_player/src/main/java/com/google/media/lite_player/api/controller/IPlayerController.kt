package com.google.media.lite_player.api.controller

import androidx.media3.common.Player
import com.google.media.lite_player.api.controller.ControllerCallback

/**
 * @author liulinjie 
 * @since 2025-07-09
 */
interface IPlayerController {
    fun registerControllerCallbacks(controllerCallback: ControllerCallback)

    fun unregisterControllerCallbacks(controllerCallback: ControllerCallback)

    fun showController()

    fun hideController()

    fun release()
        
    fun setPlayer(player: Player?)
}