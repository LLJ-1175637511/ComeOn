package com.google.media.lite_player.plugin_imp

import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import com.google.media.lite_player.plugin.BasePlayerPlugin

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-23-2025
 */
class VideoSizeChangePlugin(
    private val onSizeChangeCallback: (videoSize: VideoSize) -> Unit
) : BasePlayerPlugin {
    override fun getPlayerListener(): Player.Listener? {
        return object : Player.Listener {
            override fun onVideoSizeChanged(videoSize: VideoSize) {
                onSizeChangeCallback(videoSize)
            }
        }
    }
}