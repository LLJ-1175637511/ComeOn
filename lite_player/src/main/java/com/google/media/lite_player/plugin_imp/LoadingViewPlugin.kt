package com.google.media.lite_player.plugin_imp

import android.content.Context
import android.view.View
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.google.media.lite_player.config.Const
import com.google.media.lite_player.kit.visible_helper.VisibleAlphaAnimHelper
import com.google.media.lite_player.plugin.ViewPlayerPlugin
import com.google.media.lite_player.ui.PlayerLoadingView

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-23-2025
 */
class LoadingViewPlugin : ViewPlayerPlugin {

    private lateinit var loadingAnimHelper: VisibleAlphaAnimHelper

    override fun onCreateView(context: Context): View {
        return PlayerLoadingView(context).also {
            loadingAnimHelper = VisibleAlphaAnimHelper(
                view = it,
                duration = Const.playerConfig.alphaAnimDuration
            )
        }
    }

    override fun viewPriority(): ViewPlayerPlugin.ViewPriority = ViewPlayerPlugin.ViewPriority.MIDDLE

    override fun getPlayerListener(): Player.Listener {
        return object : Player.Listener {

            override fun onPlayerError(error: PlaybackException) {
                loadingAnimHelper.setVisible(false)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        loadingAnimHelper.setVisible(visible = true)
                    }
                    Player.STATE_READY -> {
                        loadingAnimHelper.setVisible(visible = false, useAnim = false)
                    }
                    else -> {}
                }
            }
        }
    }

}