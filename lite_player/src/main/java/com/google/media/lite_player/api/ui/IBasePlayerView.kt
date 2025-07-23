package com.google.media.lite_player.api.ui

import androidx.media3.common.Player
import com.google.media.lite_player.api.controller.IPlayerController

/**
 * @author liulinjie
 * @since 2025-07-10
 */
interface IBasePlayerView {

    fun buildSurfaceViewDelegate(): ISurfaceViewDelegate

    fun initializePlayer(player: Player?)
}