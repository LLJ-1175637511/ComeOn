package com.google.media.lite_player.plugin

import androidx.media3.common.Player

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-23-2025
 */
interface BasePlayerPlugin {

    fun getPlayerListener(): Player.Listener? = null

    fun onAddPlugin() {}

    fun onRemovePlugin() {}

    fun onVideoViewDetach() {}

}